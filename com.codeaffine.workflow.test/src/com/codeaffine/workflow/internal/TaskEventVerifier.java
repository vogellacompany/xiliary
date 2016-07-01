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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.ArgumentCaptor;

import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.event.TaskEvent;
import com.codeaffine.workflow.event.TaskListener;

class TaskEventVerifier {

  private final ArgumentCaptor<TaskEvent> captor;
  private final TaskEventNotifier notifier;
  private final TaskListener listener;

  TaskEventVerifier() {
    notifier = new TaskEventNotifier();
    listener = mock( TaskListener.class );
    notifier.addTaskListener( listener );
    captor = forClass( TaskEvent.class );
  }

  TaskEventNotifier getNotifier() {
    return notifier;
  }

  void verifyTaskCreated( Task task, TaskListImpl taskListImpl ) {
    verify( listener ).taskCreated( captor.capture() );
    verifyEventStatus( task, taskListImpl );
  }

  void verifyTaskAssigned( Task task, TaskListImpl taskListImpl ) {
    verify( listener ).taskAssigned( captor.capture() );
    verifyEventStatus( task, taskListImpl );
  }

  void verifyNoTaskAssigned( ) {
    verify( listener, never() ).taskAssigned( any( TaskEvent.class ) );
  }

  void verifyTaskAssignmentDropped( Task task, TaskListImpl taskListImpl ) {
    verify( listener ).taskAssignmentDropped( captor.capture() );
    verifyEventStatus( task, taskListImpl );
  }

  void verifyNoTaskAssignmentDropped( ) {
    verify( listener, never() ).taskAssignmentDropped( any( TaskEvent.class ) );
  }

  void verifyTaskCompleted( Task task, TaskListImpl taskListImpl ) {
    verify( listener ).taskCompleted( captor.capture() );
    verifyEventStatus( task, taskListImpl );
  }

  void verifyNoTaskCompleted( ) {
    verify( listener, never() ).taskCompleted( any( TaskEvent.class ) );
  }

  private void verifyEventStatus( Task task, TaskListImpl taskListImpl ) {
    assertThat( captor.getValue().getTask() ).isSameAs( task );
    assertThat( captor.getValue().getTaskList() ).isSameAs( taskListImpl );
  }
}