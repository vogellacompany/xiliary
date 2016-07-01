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

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ExecutableExtensionConfiguration;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

public class CreateSingleProcessor<T> extends ReadSingleProcessor<T> implements ExecutableExtensionConfiguration<T> {

  CreateSingleProcessor( CreateExecutableExtensionOperator<T> operator ) {
    super( operator );
  }

  @Override
  public CreateSingleProcessor<T> withConfiguration( ExecutableExtensionConfigurator<T> configurator ) {
    verifyNotNull( configurator, "configurator" );

    getOperator().setConfigurator( configurator );
    return this;
  }

  @Override
  public CreateSingleProcessor<T> withExceptionHandler( ExtensionExceptionHandler exceptionHandler ) {
    verifyNotNull( exceptionHandler, "exceptionHandler" );

    getOperator().setExceptionHandler( exceptionHandler );
    return this;
  }

  @Override
  public CreateSingleProcessor<T> withTypeAttribute( String typeAttribute ) {
    verifyNotNull( typeAttribute, "typeAttribute" );

    getOperator().setTypeAttribute( typeAttribute );
    return this;
  }

  private CreateExecutableExtensionOperator<T> getOperator() {
    return ( CreateExecutableExtensionOperator<T> )operator;
  }
}