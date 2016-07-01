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
package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.core.runtime.Status.CANCEL_STATUS;
import static org.eclipse.core.runtime.Status.OK_STATUS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.junit.Before;
import org.junit.Test;

public class ClearJobPDETest {

  private JobHelper jobHelper;
  private IJobChangeListener listener;
  private CompletionObserverList observers;
  private ViewerAdapter viewerAdapter;
  private PendingUpdatePlaceHolder placeholder;
  private ClearJob clearJob;

  @Before
  public void setUp() {
    listener = mock( IJobChangeListener.class );
    observers = createCompletionObserverList( listener );
    viewerAdapter = mock( ViewerAdapter.class );
    placeholder = new PendingUpdatePlaceHolder();
    clearJob = new ClearJob( observers, viewerAdapter, placeholder );
    jobHelper = new JobHelper( clearJob );
  }

  @Test
  public void initialization() {
    assertThat( clearJob.isSystem() ).isTrue();
    assertThat( clearJob.isUser() ).isFalse();
  }

  @Test
  public void schedule() {
    clearJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( viewerAdapter ).remove( placeholder );
    verify( listener ).done( any( IJobChangeEvent.class ) );
    assertThat( placeholder.isRemoved() ).isTrue();
    assertThat( clearJob.getResult() ).isSameAs( OK_STATUS );
  }

  @Test
  public void scheduleIfViewerAdapterIsDisposed() {
    when( viewerAdapter.isDisposed() ).thenReturn( true );

    clearJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( viewerAdapter, never() ).remove( placeholder );
    verify( listener).done( any( IJobChangeEvent.class ) );
    assertThat( placeholder.isRemoved() ).isFalse();
    assertThat( clearJob.getResult() ).isSameAs( CANCEL_STATUS );
  }

  @Test
  public void scheduleIfPlaceHolderHasBeenRemoved() {
    placeholder.setRemoved( true );

    clearJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( viewerAdapter, never() ).remove( placeholder );
    verify( listener).done( any( IJobChangeEvent.class ) );
    assertThat( placeholder.isRemoved() ).isTrue();
    assertThat( clearJob.getResult() ).isSameAs( CANCEL_STATUS );
  }

  private static CompletionObserverList createCompletionObserverList( IJobChangeListener listener ) {
    CompletionObserverList result = new CompletionObserverList();
    result.add( listener );
    return result;
  }
}