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

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayoutFactorySpy
  extends ScrollableLayoutFactory<Scrollable>
  implements SelectionListener, DisposeListener
{

  private SelectionEvent selectionEvent;
  private DisposeEvent disposeEvent;
  private FlatScrollBar horizontal;
  private FlatScrollBar vertical;
  private FillLayout layout;

  @Override
  public Layout create( AdaptionContext<Scrollable> context, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    this.horizontal = horizontal;
    this.vertical = vertical;
    this.layout = new FillLayout();
    return layout;
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<Scrollable> context ) {
    return this;
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<Scrollable> context ) {
    return this;
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<Scrollable> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    return this;
  }

  @Override
  public void widgetSelected( SelectionEvent selectionEvent ) {
    this.selectionEvent = selectionEvent;
  }

  @Override
  public void widgetDefaultSelected( SelectionEvent selectionEvent ) {
  }

  @Override
  public void widgetDisposed( DisposeEvent disposeEvent ) {
    this.disposeEvent = disposeEvent;
  }

  FlatScrollBar getHorizontal() {
    return horizontal;
  }

  FlatScrollBar getVertical() {
    return vertical;
  }

  SelectionEvent getSelectionEvent() {
    return selectionEvent;
  }

  DisposeEvent getDisposeEvent() {
    return disposeEvent;
  }

  Layout getLayout() {
    return layout;
  }
}