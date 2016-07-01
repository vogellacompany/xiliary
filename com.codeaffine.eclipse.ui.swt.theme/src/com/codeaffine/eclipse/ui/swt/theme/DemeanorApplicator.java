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
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.demeanorKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_EXPAND_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;

import java.util.function.BiConsumer;

import org.eclipse.swt.widgets.Scrollable;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class DemeanorApplicator {

  private final AttributeApplicator attributeApplicator;

  DemeanorApplicator( ScrollableAdapterFactory factory ) {
    attributeApplicator = new AttributeApplicator( factory );
  }

  void apply( Scrollable scrollable, String attribute, BiConsumer<ScrollbarStyle, Demeanor> demeanorSetter ) {
    attributeApplicator.applyFromBuffer( scrollable, demeanorKey( attribute ), demeanorSetter );
  }

  void apply(
    Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Demeanor> demeanorSetter )
  {
    if( attributeApplicator.canApply( scrollable ) ) {
      doApply( scrollable, value, attribute, demeanorSetter );
    } else {
      preserve( scrollable, value, attribute );
    }
  }

  private void doApply(
    Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Demeanor> demeanorSetter )
  {
    new AttributeApplicationOperation( scrollable, demeanorKey( attribute ) )
      .onDefault( preserver -> attributeApplicator.apply( scrollable, demeanorSetter, () -> getDemeanor( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( demeanorKey( attribute ), getDemeanor( value ) ) )
      .execute();
  }

  private static void preserve( Scrollable scrollable, CSSValue value, String attribute ) {
    String baseAttributeName = attribute.replaceAll( TOP_LEVEL_WINDOW_SELECTOR, "" );
    new AttributeApplicationOperation( scrollable, demeanorKey( attribute ) )
      .onDefault( preserver -> preserver.put( demeanorKey( attribute ), getDemeanor( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( demeanorKey( baseAttributeName ), getDemeanor( value ) ) )
      .execute();
  }

  static Demeanor parse( String value ) {
    verifyNotNull( value, "value" );

    if( value.equals( DEMEANOR_FIXED_WIDTH ) ) {
      return Demeanor.FIXED_SCROLL_BAR_BREADTH;
    }
    if( value.equals( DEMEANOR_EXPAND_ON_MOUSE_OVER ) ) {
      return Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER;
    }
    String message =   "'" + value + "' is not a valid value for attribute '" + ADAPTER_DEMEANOR + "'. "
                     + "Allowed values are '" + DEMEANOR_EXPAND_ON_MOUSE_OVER + "' and '" + DEMEANOR_FIXED_WIDTH + "'.";
    throw new IllegalArgumentException( message );
  }

  private static Demeanor getDemeanor( CSSValue value ) {
    return parse( value.getCssText() );
  }
}