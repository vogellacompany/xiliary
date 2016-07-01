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


import static com.codeaffine.eclipse.swt.widget.scrollable.context.OffsetComputer.DEFAULT_OFFSET;
import static com.codeaffine.eclipse.swt.widget.scrollable.context.OffsetComputer.GTK_OFFSET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class OffsetComputerTest {

  private OffsetComputer computer;
  private ScrollBar horizontalBar;
  private ScrollBar verticalBar;
  private Platform platform;

  @Before
  public void setUp() {
    verticalBar = mock( ScrollBar.class );
    horizontalBar = mock( ScrollBar.class );
    Scrollable scrollable = stubScrollable( verticalBar, horizontalBar );
    platform = mock( Platform.class );
    computer = new OffsetComputer( new ScrollableControl<>( scrollable ), platform );
  }

  @Test
  public void compute() {
    stubNonGtkPlatform();

    int actual = computer.compute();

    assertThat( actual ).isEqualTo( DEFAULT_OFFSET );
  }

  @Test
  public void computeWithStandardGtk() {
    stubGtkPlatform();
    stubVerticalBarSize( new Point( 10, 10 ) );
    stubHorizontalBarSize( new Point( 10, 10 ) );

    int actual = computer.compute();

    assertThat( actual ).isEqualTo( GTK_OFFSET );
  }

  @Test
  public void computeWithUbuntuOverlays() {
    stubGtkPlatform();
    stubVerticalBarSize( new Point( 0, 10 ) );
    stubHorizontalBarSize( new Point( 10, 0 ) );

    int actual = computer.compute();

    assertThat( actual ).isEqualTo( DEFAULT_OFFSET );
  }

  private static Scrollable stubScrollable( ScrollBar verticalBar, ScrollBar horizontalBar ) {
    Scrollable result = mock( Scrollable.class );
    when( result.getVerticalBar() ).thenReturn( verticalBar );
    when( result.getHorizontalBar() ).thenReturn( horizontalBar );
    return result;
  }

  private void stubNonGtkPlatform() {
    when( platform.matches( PlatformType.GTK ) ).thenReturn( false );
  }

  private void stubGtkPlatform() {
    when( platform.matches( PlatformType.GTK ) ).thenReturn( true );
  }

  private void stubVerticalBarSize( Point size ) {
    when( verticalBar.getSize() ).thenReturn( size );
  }

  private void stubHorizontalBarSize( Point size ) {
    when( horizontalBar.getSize() ).thenReturn( size );
  }
}