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

import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.FIXED_SCROLL_BAR_BREADTH;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.DEMEANOR_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_EXPAND_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;


public class ScrollbarPreferenceTest {

  private ScrollbarPreference preference;
  private IPreferenceStore store;

  @Before
  public void setUp() {
    store = stubPreferenceStore();
    preference = new ScrollbarPreference( ADAPTER_DEMEANOR, () -> store );
  }

  @Test
  public void apply() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );
    when( store.getString( ADAPTER_DEMEANOR ) ).thenReturn( DEMEANOR_FIXED_WIDTH );

    preference.apply( style, DEMEANOR_PREFERENCE_SETTER );

    verify( style ).setDemeanor( FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  public void applyIfNotCustomized() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );
    when( store.getString( ADAPTER_DEMEANOR ) ).thenReturn( DEMEANOR_EXPAND_ON_MOUSE_OVER );

    preference.apply( style, DEMEANOR_PREFERENCE_SETTER );

    verify( style, never() ).setDemeanor( FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  public void isCustomized() {
    when( store.getString( ADAPTER_DEMEANOR ) ).thenReturn( DEMEANOR_FIXED_WIDTH );

    boolean actual = preference.isCustomized();

    assertThat( actual ).isTrue();
  }

  @Test
  public void getValue() {
    when( store.getString( ADAPTER_DEMEANOR ) ).thenReturn( DEMEANOR_FIXED_WIDTH );

    String actual = preference.getValue();

    assertThat( actual ).isSameAs( DEMEANOR_FIXED_WIDTH );
  }

  private static IPreferenceStore stubPreferenceStore() {
    IPreferenceStore result = mock( IPreferenceStore.class );
    when( result.getDefaultString( ADAPTER_DEMEANOR ) ).thenReturn( DEMEANOR_EXPAND_ON_MOUSE_OVER );
    return result;
  }
}