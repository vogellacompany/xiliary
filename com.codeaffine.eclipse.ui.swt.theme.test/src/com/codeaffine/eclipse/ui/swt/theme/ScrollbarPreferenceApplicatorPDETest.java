package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.FIXED_SCROLL_BAR_BREADTH;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeApplicator.attach;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.demeanorKey;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.integerKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollbarPreferencesInitializer.UNSET;
import static java.lang.Integer.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class ScrollbarPreferenceApplicatorPDETest {

  @Rule public final ScrollbarPreferenceRule scrollbarPreferenceRule = new ScrollbarPreferenceRule();
  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private AttributePreserver attributePreserver;
  private ScrollbarStyle style;

  @Before
  public void setUp() {
    scrollbarPreferenceRule.addListener( new ScrollbarPreferenceApplicator() );
    Tree scrollable = new Tree( displayHelper.createShell(), SWT.NONE );
    attributePreserver = new AttributePreserver( scrollable );
    style = new ScrollableAdapterFactory().create( scrollable, TreeAdapter.class );
    attach( scrollable, style );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyDemeanorFromPreference() {
    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, DEMEANOR_FIXED_WIDTH );

    assertThat( style.getDemeanor() ).isSameAs( FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyDemeanorFromDefaultValue() {
    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, DEMEANOR_FIXED_WIDTH );

    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, UNSET );

    assertThat( style.getDemeanor() ).isSameAs( EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyDemeanorFromCssBuffer() {
    attributePreserver.put( demeanorKey( ADAPTER_DEMEANOR ), EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, DEMEANOR_FIXED_WIDTH );

    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, UNSET );

    assertThat( style.getDemeanor() ).isSameAs( EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyIncrementLengthFromPreference() {
    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, "1" );

    assertThat( style.getIncrementButtonLength() ).isSameAs( 1 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyIncrementLengthFromDefaultValue() {
    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, "1" );

    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );

    assertThat( style.getIncrementButtonLength() ).isSameAs( 0 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyIncrementLengthFromCssBuffer() {
    attributePreserver.put( integerKey( FLAT_SCROLL_BAR_INCREMENT_LENGTH ), valueOf( 1 ) );
    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, "3" );

    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, UNSET );

    assertThat( style.getIncrementButtonLength() ).isSameAs( 1 );
  }
}