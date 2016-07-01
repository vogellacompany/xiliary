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
package com.codeaffine.workflow.internal;

import static java.lang.String.format;

import com.codeaffine.workflow.NodeDefinition;

class DecisionVerificator {

  static final String ERROR_UNREACHABLE_NODE = "Node <%s> is not reachable from node <%s>.";

  void verify( NodeDefinition nodeDefinition, String succesor ) {
    if( !isDefined( nodeDefinition, succesor ) ) {
      throw new IllegalStateException( format( ERROR_UNREACHABLE_NODE, succesor, nodeDefinition.getNodeId() ) );
    }
  }

  private static boolean isDefined( NodeDefinition nodeDefinition, String succesor ) {
    boolean found = false;
    for( String successor : nodeDefinition.getSuccessors() ) {
      found |= successor.equals( succesor );
    }
    return found;
  }
}