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

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;
import static java.lang.Math.max;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class StructuredScrollableLayouter implements ScrollableLayouter {

  private final HorizontalSelectionComputer horizontalSelectionComputer;
  private final ScrollableControl<? extends Scrollable> scrollable;

  StructuredScrollableLayouter( ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollable = scrollable;
    this.horizontalSelectionComputer = new HorizontalSelectionComputer();
  }

  @Override
  public void layout( AdaptionContext<?> context ) {
    scrollable.setSize( computeWidth( context ), computeHeight( context ) );
    context.adjustPreferredWidthIfHorizontalBarIsVisible();
    scrollable.setSize( computeWidth( context ), computeHeight( context ) );
    scrollable.setLocation( computeLocation( context ) );
  }

  private Point computeLocation( AdaptionContext<?> context ) {
    int selection = horizontalSelectionComputer.compute( context );
    int x = context.getOriginOfScrollableOrdinates().x - selection - context.getOffset();
    int y = context.getOriginOfScrollableOrdinates().y - context.getOffset();
    return new Point( x, y );
  }

  private static int computeWidth( AdaptionContext<?> context ) {
    int result = max( context.getPreferredSize().x, context.getVisibleArea().width );
    if( context.isVerticalBarVisible() ) {
      result = computeWidthWithVerticalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeWidthWithVerticalBarPadding( AdaptionContext<?> context ) {
    int preferredWidth = context.getPreferredSize().x;
    int visibleAreaWidth = context.getVisibleArea().width;
    int offset = context.getVerticalBarOffset();
    return max( preferredWidth + offset, visibleAreaWidth + offset );
  }

  private static int computeHeight( AdaptionContext<?> context ) {
    int result = context.getVisibleArea().height;
    if( context.isHorizontalBarVisible() ) {
      result = computeHeightWithHorizontalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeHeightWithHorizontalBarPadding( AdaptionContext<?> context ) {
    return context.getVisibleArea().height - BAR_BREADTH;
  }
}