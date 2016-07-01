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
package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.ExtensionException;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;

class ExtensionCreator<T> {

  final ExtensionExceptionHandler exceptionHandler;
  final ExecutableExtensionConfigurator<T> configurator;
  final IConfigurationElement element;
  final Class<T> extensionType;
  final String typeAttribute;

  ExtensionCreator( IConfigurationElement element,
                    Class<T> extensionType,
                    ExtensionExceptionHandler exceptionHandler,
                    ExecutableExtensionConfigurator<T> configurator,
                    String typeAttribute )
  {
    this.configurator = configurator;
    this.typeAttribute = typeAttribute;
    this.exceptionHandler = exceptionHandler;
    this.extensionType = extensionType;
    this.element = element;
  }

  T create() throws ExtensionException {
    T result = extensionType.cast( createExecutableExtension( element ) );
    configurator.configure( result, new Extension( element ) );
    return result;
  }

  private Object createExecutableExtension( IConfigurationElement element ) {
    try {
      return element.createExecutableExtension( typeAttribute );
    } catch( CoreException coreException ) {
      exceptionHandler.handle( coreException );
      return null;
    }
  }
}