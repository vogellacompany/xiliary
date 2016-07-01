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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

public class AdaptionContext<T extends Scrollable> {

  public static final int OVERLAY_OFFSET = 40;

  private final ScrollbarVisibility scrollbarVisibility;
  private final Point originOfScrollableOrdinates;
  private final Map<Class<?>, Object> attributes;
  private final ScrollableControl<T> scrollable;
  private final Reconciliation reconciliation;
  private final SizeComputer sizeComputer;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Composite adapter;
  private final int borderWidth;
  private final int itemHeight;
  private final int offset;

  public AdaptionContext( Composite adapter, ScrollableControl<T> scrollable ) {
    this( adapter, scrollable, 1, null, new SizeComputer( scrollable, adapter ), new HashMap<>() );
  }

  private AdaptionContext( Composite adapter,
                           ScrollableControl<T> scrollable,
                           int itemHeight,
                           Reconciliation reconciliation,
                           SizeComputer sizeComputer,
                           Map<Class<?>, Object> attributes )
  {
    this.attributes = attributes;
    this.sizeComputer = sizeComputer;
    this.scrollbarVisibility = new ScrollbarVisibility( sizeComputer, scrollable, adapter.getClientArea(), itemHeight );
    this.reconciliation = reconciliation == null ? new Reconciliation( adapter, scrollable ) : reconciliation;
    this.verticalBarOffset = computeVerticalBarOffset( scrollable );
    this.offset = new OffsetComputer( scrollable ).compute();
    this.visibleArea = computeVisibleArea( adapter, scrollable );
    this.borderWidth = scrollable.getBorderWidth();
    this.originOfScrollableOrdinates = new Point( visibleArea.x - borderWidth, visibleArea.y - borderWidth );
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.itemHeight = itemHeight;
  }

  public AdaptionContext<T> newContext( int itemHeight ) {
    return new AdaptionContext<T>( adapter, scrollable, itemHeight, reconciliation, sizeComputer, attributes );
  }

  public AdaptionContext<T> newContext() {
    return new AdaptionContext<T>( adapter, scrollable, itemHeight, reconciliation, sizeComputer, attributes );
  }

  public <A> void put( Class<A> key, A value ) {
    attributes.put( key, value );
  }

  public <A> A get( Class<A> key ) {
    return key.cast( attributes.get( key ) );
  }

  public Reconciliation getReconciliation() {
    return reconciliation;
  }

  public Composite getAdapter() {
    return adapter;
  }

  public ScrollableControl<T> getScrollable() {
    return scrollable;
  }

  public int getOffset() {
    return offset;
  }

  public Point getOriginOfScrollableOrdinates() {
    return originOfScrollableOrdinates;
  }

  public Point getPreferredSize() {
    return sizeComputer.getPreferredSize();
  }

  public void updatePreferredSize() {
    sizeComputer.updatePreferredSize();
  }

  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    sizeComputer.adjustPreferredWidthIfHorizontalBarIsVisible();
  }

  public boolean isVerticalBarVisible() {
    return scrollbarVisibility.isVerticalBarVisible();
  }

  public boolean isHorizontalBarVisible() {
    return scrollbarVisibility.isHorizontalBarVisible();
  }

  public Rectangle getVisibleArea() {
    return visibleArea;
  }

  public int getHorizontalAdapterSelection() {
    ScrollBar horizontalBar = getAdapter().getHorizontalBar();
    if( horizontalBar != null ) {
      return horizontalBar.getSelection();
    }
    return 0;
  }

  public int getVerticalBarOffset() {
    return verticalBarOffset;
  }

  public boolean isScrollableReplacedByAdapter() {
    return scrollable.isChildOf( adapter.getParent() );
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  private static int computeVerticalBarOffset( ScrollableControl<? extends Scrollable> scrollable ) {
    int result = scrollable.getVerticalBarSize().x;
    if( result == 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }

  private static Rectangle computeVisibleArea( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    Rectangle area = adapter.getClientArea();
    int borderAdjustment = scrollable.getBorderWidth() * 2;
    return new Rectangle( area.x, area.y, area.width + borderAdjustment, area.height + borderAdjustment );
  }
}