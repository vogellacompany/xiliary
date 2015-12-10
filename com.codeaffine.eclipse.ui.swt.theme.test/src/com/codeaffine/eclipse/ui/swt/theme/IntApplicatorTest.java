package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssIntValue;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssStringValue;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

public class IntApplicatorTest {

  private static final CSSPrimitiveValue CSS_INCREMENT = stubCssIntValue( 7 );

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ApplicatorTestHelper applicatorTestHelper;
  private IntApplicator applicator;
  private Scrollable scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new Tree( shell, SWT.NONE );
    applicatorTestHelper = new ApplicatorTestHelper( scrollable );
    applicator = new IntApplicator( applicatorTestHelper.getFactory() );
  }

  @Test
  public void apply() {
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyTopLevelWindowAttribute() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyTopLevelWindowAttributeOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isNotEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyWithBuffering() {
    applicator.apply( scrollable, CSS_INCREMENT, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyTopLevelWindowAttributeWithBuffering() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyTopLevelWindowAttributeWithBufferingOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isNotEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  public void applyWithNegativeCSSValue() {
    applicatorTestHelper.adapt();

    Throwable actual = thrownBy( () -> {
      CSSPrimitiveValue negativeInt = stubCssIntValue( -1 );
      applicator.apply( scrollable, negativeInt, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    } );

    assertThat( actual )
      .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .isInstanceOf( IllegalArgumentException.class );
  }
  @Test
  public void applyWithNonIntCSSValue() {
    applicatorTestHelper.adapt();

    Throwable actual = thrownBy( () -> {
      CSSPrimitiveValue bad = stubCssStringValue( "bad" );
      applicator.apply( scrollable, bad, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    } );

    assertThat( actual )
    .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
    .hasMessageContaining( "bad" )
    .isInstanceOf( IllegalArgumentException.class );
  }

  private static int expectedValue( CSSPrimitiveValue cssValue ) {
    return parseInt( cssValue.getCssText() );
  }
}