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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class StructureScrollableRedrawInsurance {

  private final ScrollableControl<?> scrollable;

  private int actualRedrawRequestCount;
  private int previousRedrawRequestCount;

  StructureScrollableRedrawInsurance( ScrollableControl<?> scrollable ) {
    this.scrollable = scrollable;
    if( mustRedrawOnVerticalScrolling( scrollable ) ) {
      captureVerticalScrolling( scrollable.getControl() );
    }
  }

  private static boolean mustRedrawOnVerticalScrolling( ScrollableControl<?> scrollable ) {
    return    scrollable.isOwnerDrawn()
           && scrollable.hasStyle( SWT.VIRTUAL )
           && scrollable.isStructuredScrollableType()
           && scrollable.hasStyle( SWT.V_SCROLL );
  }

  private void captureVerticalScrolling( Scrollable scrollable ) {
    scrollable.getVerticalBar().addListener( SWT.Selection, evt -> mark() );
  }

  private void mark() {
    actualRedrawRequestCount++;
  }

  public void run() {
    if( actualRedrawRequestCount > 0 ) {
      if( previousRedrawRequestCount == actualRedrawRequestCount ) {
        scrollable.getControl().redraw();
        actualRedrawRequestCount = 0;
        previousRedrawRequestCount = 0;
      } else {
        previousRedrawRequestCount = actualRedrawRequestCount;
      }
    }
  }
}