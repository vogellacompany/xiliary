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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class NestingStructurePreserverTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void runIfAdapterStructureIsCorrupted() {
    Shell parent = displayHelper.createShell();
    Composite adapter = new Composite( parent, SWT.NONE );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( new Tree( parent, SWT.NONE ) );
    NestingStructurePreserver preserver = new NestingStructurePreserver( scrollableControl, adapter );

    preserver.run();

    assertThat( parent.getChildren() ).doesNotContain( scrollableControl.getControl() );
    assertThat( adapter.getChildren() ).contains( scrollableControl.getControl() );
    assertThat( scrollableControl.getControl().getParent() ).isSameAs( parent );
  }

  @Test
  public void runIfAdapterStructureIsOk() {
    Shell parent = displayHelper.createShell();
    Composite adapter = new Composite( parent, SWT.NONE );
    ScrollableControl<?> scrollableControl = new ScrollableControl<>( new Tree( parent, SWT.NONE ) );
    NestingStructurePreserver preserver = new NestingStructurePreserver( scrollableControl, adapter );
    scrollableControl.setParent( adapter );

    preserver.run();

    assertThat( parent.getChildren() ).doesNotContain( scrollableControl.getControl() );
    assertThat( adapter.getChildren() ).contains( scrollableControl.getControl() );
    assertThat( scrollableControl.getControl().getParent() ).isSameAs( adapter );
  }
}