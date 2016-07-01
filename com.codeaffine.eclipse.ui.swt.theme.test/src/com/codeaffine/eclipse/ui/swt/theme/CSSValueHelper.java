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

import static java.lang.Integer.toHexString;
import static java.lang.String.valueOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_RGBCOLOR;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import org.eclipse.e4.ui.css.core.css2.CSS2RGBColorImpl;
import org.w3c.dom.css.CSSPrimitiveValue;

@SuppressWarnings("restriction")
class CSSValueHelper {

  static CSSPrimitiveValue stubCssColorValue( int red, int green, int blue ) {
    CSSPrimitiveValue result = mock( CSSPrimitiveValue.class );
    CSS2RGBColorImpl value = new CSS2RGBColorImpl( red, green, blue );
    when( result.getCssValueType() ).thenReturn( CSS_PRIMITIVE_VALUE );
    when( result.getPrimitiveType() ).thenReturn( CSS_RGBCOLOR );
    when( result.getRGBColorValue() ).thenReturn( value );
    when( result.getCssText() ).thenReturn( "#" + toHexString( red ) + toHexString( green ) + toHexString( blue ) );
    return result;
  }

  static CSSPrimitiveValue stubCssBooleanValue( boolean booleanValue ) {
    return stubCssStringValue( valueOf( booleanValue ) );
  }

  static CSSPrimitiveValue stubCssIntValue( int intValue ) {
    return stubCssStringValue( valueOf( intValue ) );
  }

  static CSSPrimitiveValue stubCssStringValue( String stringValue ) {
    CSSPrimitiveValue result = mock( CSSPrimitiveValue.class );
    when( result.getCssText() ).thenReturn( stringValue );
    return result;
  }
}