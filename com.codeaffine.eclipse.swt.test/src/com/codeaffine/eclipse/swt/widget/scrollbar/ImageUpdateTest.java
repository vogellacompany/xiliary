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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.ImageDrawer.IMAGE_DRAWER_IS_DISPOSED;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ImageUpdateTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ImageUpdate update;
  private Label control;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    control = new Label( shell, SWT.NONE );
    update = new ImageUpdate( control, FlatScrollBar.DEFAULT_MAX_EXPANSION );
    shell.open();
  }

  @Test
  public void update() {
    update.update();
    Image actual = control.getImage();

    assertThat( actual.getBounds() ).isEqualTo( expectedImageBounds() );
  }

  @Test
  public void updateOnTooSmallControlWidth() {
    control.setSize( 0, 10 );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNull();
  }

  @Test
  public void updateOnTooSmallControlHeight() {
    control.setSize( 10, 0 );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNull();
  }

  @Test
  public void updateWithPreviousImage() {
    Image oldImage = displayHelper.createImage( 10, 20 );
    control.setImage( oldImage );

    update.update();
    Image actual = control.getImage();

    assertThat( actual ).isNotSameAs( oldImage );
    assertThat( oldImage.isDisposed() ).isTrue();
  }

  @Test
  public void updateWithDifferntColors() {
    update.update();
    ImageData first = control.getImage().getImageData();
    update.setForeground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED ) );
    update.update();
    ImageData second = control.getImage().getImageData();

    assertThat( first.data ).isNotEqualTo( second.data );
  }

  @Test
  public void updateOnDisposedControl() {
    control.dispose();

    Throwable actual = thrownBy( () -> update.update() );

    assertThat( actual ).isNull();
  }

  @Test
  public void setForeground() {
    Color expected = displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED );

    update.setForeground( expected );
    Color actual = update.getForeground();

    assertThat( actual )
      .isEqualTo( expected )
      .isNotSameAs( expected );
  }

  @Test
  public void setBackground() {
    Color expected = displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED );

    update.setBackground( expected );
    Color actual = update.getBackground();

    assertThat( actual )
      .isEqualTo( expected )
      .isNotSameAs( expected );
  }

  @Test
  public void getBackgroundIfControlIsDisposed() {
    control.dispose();

    Throwable actual = thrownBy( () -> update.getBackground() );

    assertThat( actual )
      .hasMessage( IMAGE_DRAWER_IS_DISPOSED )
      .isInstanceOf( IllegalStateException.class );
  }

  private Rectangle expectedImageBounds() {
    return new Rectangle( 0, 0, control.getSize().x, control.getSize().y );
  }
}