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
package com.codeaffine.workflow.persistence;

import java.util.List;

import com.codeaffine.workflow.TaskHolder;

public class Memento {

  private final List<TaskHolder> content;
  
  public Memento( List<TaskHolder> content ) {
    this.content = content;
  }
  
  public List<TaskHolder> getContent() {
    return content;
  }
}