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
package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ExtensionExceptionTest {

  private static final String MESSAGE = "message";

  @Test
  public void constructorWithCause() {
    Throwable cause = new Throwable( MESSAGE );

    ExtensionException actual = new ExtensionException( cause );

    assertThat( actual )
      .hasCauseExactlyInstanceOf( Throwable.class )
      .hasMessage( cause.getClass().getName() + ": " + MESSAGE );
  }

  @Test
  public void constructorWithNullAsCause() {
    ExtensionException actual = new ExtensionException( null );

    assertThat( actual )
    .hasMessage( null )
    .hasNoCause();
  }
}