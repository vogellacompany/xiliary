package com.codeaffine.workflow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class TaskEventTest {

  private static final TaskList TASK_LIST = mock( TaskList.class );
  private static final Task TASK = mock( Task.class );

  @Test
  public void getTask() {
    TaskEvent event = new TaskEvent( TASK, TASK_LIST );

    Task actual = event.getTask();

    assertThat( actual ).isSameAs( TASK );
  }

  @Test
  public void getTaskList() {
    TaskEvent event = new TaskEvent( TASK, TASK_LIST );

    TaskList actual = event.getTaskList();

    assertThat( actual ).isSameAs( TASK_LIST );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsTask() {
    new TaskEvent( null, TASK_LIST );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsTaskList() {
    new TaskEvent( TASK, null );
  }
}
