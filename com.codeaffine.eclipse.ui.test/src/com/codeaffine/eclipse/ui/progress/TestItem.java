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
package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.ui.progress.ThreadHelper.sleep;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;

class TestItem implements IAdaptable {

  static final int FETCH_CHILDREN_DELAY = 10;

  private final TestItemAdapter testItemAdapter;
  private final Collection<TestItem> children;
  private final TestItem parent;
  private final String name;

  private boolean ignoreAdapter;

  TestItem( TestItem parent, String name ) {
    this.testItemAdapter = new TestItemAdapter();
    this.children = new ArrayList<TestItem>();
    this.parent = parent;
    this.name = name;
  }

  String getName() {
    return name;
  }

  TestItem getParent() {
    return parent;
  }

  Collection<TestItem> getChildren() {
    simulateFetchDelay();
    return children;
  }

  @Override
  @SuppressWarnings({
    "unchecked",
    "rawtypes"
  })
  public Object getAdapter( Class adapter ) {
    Object result = null;
    if( !ignoreAdapter && adapter == IDeferredWorkbenchAdapter.class ) {
      result = testItemAdapter;
    } else if( !ignoreAdapter && adapter == IWorkbenchAdapter.class ) {
      result = testItemAdapter;
    }
    return result;
  }

  void ignoreAdpater() {
    ignoreAdapter = true;
  }

  private void simulateFetchDelay() {
    if( !children.isEmpty() ) {
      sleep( FETCH_CHILDREN_DELAY );
    }
  }
}