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

import static com.codeaffine.eclipse.ui.swt.theme.AttributeApplicator.attach;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_DEMEANOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.DEMEANOR_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_THUMB_COLOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.INCREMENT_LENGTH_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.ControlElements.extractControl;
import static com.codeaffine.eclipse.ui.swt.theme.ControlElements.extractScrollable;
import static com.codeaffine.eclipse.ui.swt.theme.ControlElements.isControlElement;
import static com.codeaffine.eclipse.ui.swt.theme.TypeToAdapterMapping.tryFindTypeToAdapterMapping;
import static java.lang.Boolean.parseBoolean;

import java.util.Optional;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.swt.widgets.Scrollable;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

@SuppressWarnings( "restriction" )
public class ScrollableAdapterContribution implements ICSSPropertyHandler {

  public static final String FLAT_SCROLL_BAR_PAGE_INCREMENT = "flat-scroll-bar-page-increment";
  public static final String FLAT_SCROLL_BAR_BACKGROUND = "flat-scroll-bar-background";
  public static final String FLAT_SCROLL_BAR_THUMB = "flat-scroll-bar-thumb";
  public static final String FLAT_SCROLL_BAR_INCREMENT_LENGTH = "flat-scroll-bar-increment-length";
  public static final String FLAT_SCROLL_BAR = "flat-scroll-bar";
  public static final String ADAPTER_DEMEANOR = "adapter-demeanor";
  public static final String ADAPTER_BACKGROUND = "adapter-background";
  public static final String DEMEANOR_FIXED_WIDTH = "fixed-width";
  public static final String DEMEANOR_EXPAND_ON_MOUSE_OVER = "expand-on-mouse-over";
  public static final String TOP_LEVEL_WINDOW_SELECTOR = "-top-level";

  private final ScrollbarPreference incrementButtonLengthPreference;
  private final ScrollbarPreference demeanorPreference;
  private final DemeanorApplicator demeanorApplicator;
  private final ScrollableAdapterFactory factory;
  private final ColorApplicator colorApplicator;
  private final N0Applicator n0Applicator;

  public ScrollableAdapterContribution() {
    incrementButtonLengthPreference = new ScrollbarPreference( FLAT_SCROLL_BAR_INCREMENT_LENGTH );
    demeanorPreference = new ScrollbarPreference( ADAPTER_DEMEANOR );
    factory = new ScrollableAdapterFactory();
    colorApplicator = new ColorApplicator( factory );
    demeanorApplicator = new DemeanorApplicator( factory );
    n0Applicator = new N0Applicator( factory );
  }

  @Override
  public String retrieveCSSProperty( Object element, String property, String pseudo, CSSEngine engine )
    throws Exception
  {
    return null;
  }

  @Override
  public boolean applyCSSProperty( Object element, String property, CSSValue value, String pseudo, CSSEngine engine )
    throws Exception
  {
    if( mustApply( element ) ) {
      doApplyCssProperty( extractScrollable( element ), property, value );
    }
    return false;
  }

  private static boolean mustApply( Object element ) {
    return isControlElement( element ) && tryFindTypeToAdapterMapping( extractControl( element ) ).isPresent();
  }

  private void doApplyCssProperty( Scrollable element, String property, CSSValue value ) {
    switch( property ) {
      case FLAT_SCROLL_BAR:
        adapt( element, value );
        colorApplicator.apply( element, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );
        colorApplicator.apply( element, FLAT_SCROLL_BAR_THUMB, FLAT_SCROLLBAR_THUMB_COLOR_SETTER );
        colorApplicator.apply( element, FLAT_SCROLL_BAR_PAGE_INCREMENT, FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER );
        n0Applicator.apply( element, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
        colorApplicator.apply( element, ADAPTER_BACKGROUND, ADAPTER_BACKGROUND_SETTER );
        demeanorApplicator.apply( element, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );
        break;
      case FLAT_SCROLL_BAR_BACKGROUND:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_BACKGROUND_SETTER );
        break;
      case FLAT_SCROLL_BAR_THUMB:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_THUMB_COLOR_SETTER );
        break;
      case FLAT_SCROLL_BAR_PAGE_INCREMENT:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER );
        break;
      case FLAT_SCROLL_BAR_INCREMENT_LENGTH:
        n0Applicator.apply( element, wrap( property, value ), property, FLAT_SCROLLBAR_INCREMENT_SETTER );
        break;
      case ADAPTER_DEMEANOR:
        demeanorApplicator.apply( element, wrap( property, value ), property, ADAPTER_DEMEANOR_SETTER );
        break;
      case ADAPTER_BACKGROUND:
        colorApplicator.apply( element, value, property, ADAPTER_BACKGROUND_SETTER );
        break;
      case FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_BACKGROUND_SETTER );
        break;
      case FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_THUMB_COLOR_SETTER );
        break;
      case FLAT_SCROLL_BAR_PAGE_INCREMENT + TOP_LEVEL_WINDOW_SELECTOR:
        colorApplicator.apply( element, value, property, FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER );
        break;
      case FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR:
        CSSValue incrementButtonValue = wrap( FLAT_SCROLL_BAR_INCREMENT_LENGTH, value );
        n0Applicator.apply( element, incrementButtonValue, property, FLAT_SCROLLBAR_INCREMENT_SETTER );
        break;
      case ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR:
        demeanorApplicator.apply( element, wrap( ADAPTER_DEMEANOR, value ), property, ADAPTER_DEMEANOR_SETTER );
        break;
      case ADAPTER_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR:
        colorApplicator.apply( element, value, property, ADAPTER_BACKGROUND_SETTER );
        break;
      default:
        throw new IllegalArgumentException( "Unsupported attribute '" + property + "'" );
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void adapt( Scrollable scrollable, CSSValue value ) {
    if( !factory.isAdapted( scrollable ) && parseBoolean( value.getCssText() ) ) {
      TypeToAdapterMapping<? extends Scrollable, ? extends Adapter> mapping = lookupMapping( scrollable );
      Scrollable scrollableExtension = mapping.scrollableType.cast( scrollable );
      Optional adapter = factory.create( scrollableExtension, mapping.adapterType );
      if( adapter.isPresent() ) {
        ScrollbarStyle result = ( ScrollbarStyle )adapter.get();
        attach( scrollable, result );
        incrementButtonLengthPreference.apply( result, INCREMENT_LENGTH_PREFERENCE_SETTER );
        demeanorPreference.apply( result, DEMEANOR_PREFERENCE_SETTER );
      }
    }
  }

  @SuppressWarnings("rawtypes")
  private static TypeToAdapterMapping<? extends Scrollable, ? extends Adapter> lookupMapping( Scrollable scrollable ) {
    return tryFindTypeToAdapterMapping( scrollable ).get();
  }

  private static CSSValue wrap( String property, CSSValue value ) {
    return new CssOrPreferenceValue( property, value );
  }
}