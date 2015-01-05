package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_CONTEXT;
import static com.codeaffine.workflow.WorkflowContext.VARIABLE_TASK_LIST;

import com.codeaffine.workflow.Activity;
import com.codeaffine.workflow.ActivityAspect;
import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.Task;
import com.codeaffine.workflow.VariableDeclaration;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.WorkflowDefinition;
import com.codeaffine.workflow.persistence.WorkflowMemento;

public class WorkflowImpl implements Workflow {

  private final WorkflowDefinitionImpl definition;
  private final ActivityExecutor activityExecutor;
  private final OperationPointer operationPointer;
  private final WorkflowContextImpl context;
  private final NodeLoader nodeLoader;
  private final TaskListImpl taskList;

  public WorkflowImpl(
    WorkflowDefinition definition, TaskListImpl taskList, FlowEventNotifier notifier, NodeLoader nodeLoader )
  {
    this.definition = ( WorkflowDefinitionImpl )definition;
    this.taskList = taskList;
    this.nodeLoader = nodeLoader;
    this.context = new WorkflowContextImpl();
    this.activityExecutor = new ActivityExecutor();
    this.operationPointer = new OperationPointer( nodeLoader, notifier, context, this.definition );
    defineDefaultVariables();
  }

  public String getDefinitionId() {
    return definition.getId();
  }

  @Override
  public boolean matches( Object value ) {
    boolean result = false;
    if( definition.getMatcher() != null ) {
      result = definition.getMatcher().matches( value );
    }
    return result;
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public void copyContext( WorkflowContext workflowContext ) {
    for( VariableDeclaration<?> variableDeclaration : workflowContext.getVariableDeclarations() ) {
      VariableDeclaration<Object> variableDeclaration2 = ( VariableDeclaration<Object> )variableDeclaration;
      defineVariable( variableDeclaration2, workflowContext.getVariableValue( variableDeclaration ) );
    }
  }

  @Override
  public <T> void defineVariable( VariableDeclaration<T> declaration, T value ) {
    context.defineVariable( declaration, value );
  }

  @Override
  public WorkflowContext getContext() {
    return context;
  }

  public WorkflowMemento save() {
    return new WorkflowMemento( operationPointer.save(), context.save() );
  }

  public void restore( WorkflowMemento memento ) {
    operationPointer.restore( memento.getPointerMemento() );
    context.restore( memento.getContextMemento() );
    defineDefaultVariables();
  }

  @Override
  public void addActivityAspect( ActivityAspect activityConditionCheck ) {
    activityExecutor.add( activityConditionCheck );
  }

  @Override
  public void start() {
    continueWorkflow();
  }

  public void continueWorkflow() {
    operationPointer.move();
    if( operationPointer.isAvailable() ) {
      handleNode( operationPointer.acquire() );
    }
  }

  private void handleNode( NodeDefinition nodeDefinition ) {
    Class<?> type = nodeDefinition.getType();
    if( Activity.class.isAssignableFrom( type ) ) {
      Activity activity = ( Activity )nodeLoader.load( type, context );
      activityExecutor.execute( activity );
      continueWorkflow();
    } else {
      Task task = ( Task )nodeLoader.load( type, context );
      taskList.add( task, this );
    }
  }

  private void defineDefaultVariables() {
    defineVariable( VARIABLE_CONTEXT, context );
    defineVariable( VARIABLE_TASK_LIST, taskList );
  }
}