/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.internal;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static com.codeaffine.workflow.internal.TaskListAssert.assertThat;
import static com.codeaffine.workflow.internal.WorkflowServiceImpl.ERROR_DEFINITON_NOT_FOUND;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.event.FlowEvent;
import com.codeaffine.workflow.event.FlowListener;
import com.codeaffine.workflow.event.TaskEvent;
import com.codeaffine.workflow.event.TaskListener;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class WorkflowServiceImplTest {

  private static final String ID = "id";

  private WorkflowServiceImpl service;

  static class DefinitionProvider implements WorkflowDefinitionProvider {

    @Override
    public void define( WorkflowDefinition definition ) {
      definition.setId( ID );
      definition.addActivity( OPERATION_ID, TestActivity.class, null );
      definition.setStart( OPERATION_ID );
    }
  }

  static class TaskDefinitionProvider implements WorkflowDefinitionProvider {

    @Override
    public void define( WorkflowDefinition definition ) {
      definition.setId( ID );
      definition.addTask( OPERATION_ID, TestTask.class, null );
      definition.setStart( OPERATION_ID );
    }
  }

  @Before
  public void setUp() {
    service = new WorkflowServiceImpl();
  }

  @Test
  public void create() {
    service.addWorkflowDefinition( new DefinitionProvider() );

    Workflow workflow = service.create( ID );

    assertThat( workflow ).isNotNull();
    assertThat( workflow.getContext().getVariableValue( VARIABLE_SERVICE ) ).isSameAs( service );
  }

  @Test
  public void createWithNonExistingId() {
    Throwable actual = thrownBy( () -> service.create( ID ) );

    assertThat( actual )
      .hasMessage( format( ERROR_DEFINITON_NOT_FOUND, ID ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void getIds() {
    service.addWorkflowDefinition( new DefinitionProvider() );

    String[] ids = service.getWorkflowDefinitionIds();

    assertThat( ids )
      .hasSize( 1 )
      .contains( ID );
  }

  @Test
  public void removeWorkflowDefinition() {
    service.addWorkflowDefinition( new DefinitionProvider() );

    service.removeWorkflowDefinition( new DefinitionProvider() );
    String[] actual = service.getWorkflowDefinitionIds();

    assertThat( actual ).isEmpty();
  }

  @Test( expected = IllegalArgumentException.class )
  public void addInvalidWorkflowDefinition() {
    service.addWorkflowDefinition( mock( WorkflowDefinitionProvider.class ) );
  }

  @Test
  public void flowEventNotification() {
    FlowListener listener = mock( FlowListener.class );
    service.addFlowListener( listener );
    service.addWorkflowDefinition( new DefinitionProvider() );
    Workflow workflow = service.create( ID );

    workflow.start();

    verify( listener ).onNodeEnter( any( FlowEvent.class ) );
    verify( listener ).onNodeLeave( any( FlowEvent.class ) );
  }

  @Test
  public void removeFlowListener() {
    FlowListener listener = mock( FlowListener.class );
    service.addFlowListener( listener );
    service.addWorkflowDefinition( new DefinitionProvider() );
    Workflow workflow = service.create( ID );

    service.removeFlowListener( listener );
    workflow.start();

    verify( listener, never() ).onNodeEnter( any( FlowEvent.class ) );
    verify( listener, never() ).onNodeLeave( any( FlowEvent.class ) );
  }

  @Test
  public void taskEventNotification() {
    TaskListener listener = mock( TaskListener.class );
    service.addTaskListener( listener );
    service.addWorkflowDefinition( new TaskDefinitionProvider() );
    Workflow workflow = service.create( ID );

    workflow.start();

    verify( listener ).taskCreated( any( TaskEvent.class ) );
    assertThat( service.getTaskList() ).hasSize( 1 );
  }

  @Test
  public void removeTaskListener() {
    TaskListener listener = mock( TaskListener.class );
    service.addTaskListener( listener );
    service.addWorkflowDefinition( new TaskDefinitionProvider() );
    Workflow workflow = service.create( ID );

    service.removeTaskListener( listener );
    workflow.start();

    verify( listener, never() ).taskCreated( any( TaskEvent.class ) );
    assertThat( service.getTaskList() ).hasSize( 1 );
  }

  @Test
  public void defineVariable() {
    WorkflowService newService = mock( WorkflowService.class );
    WorkflowService oldService = service.defineVariable( VARIABLE_SERVICE, newService );

    service.addWorkflowDefinition( new DefinitionProvider() );
    Workflow workflow = service.create( ID );

    assertThat( oldService ).isSameAs( service );
    assertThat( workflow ).isNotNull();
    assertThat( workflow.getContext().getVariableValue( VARIABLE_SERVICE ) ).isSameAs( newService );
  }
}