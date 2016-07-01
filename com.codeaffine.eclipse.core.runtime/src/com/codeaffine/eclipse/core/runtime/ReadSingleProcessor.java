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

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import java.util.function.Predicate;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

public class ReadSingleProcessor<T> {

  protected final ReadExtensionOperator<T> operator;

  ReadSingleProcessor( ReadExtensionOperator<T> operator ) {
    this.operator = operator;
  }

  public ReadSingleProcessor<T> thatMatches( Predicate<Extension> predicate ) {
    verifyNotNull( predicate, "predicate" );

    operator.setPredicate( predicate );
    return this;
  }

  public T process() {
    return operator.create();
  }
}