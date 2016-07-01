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

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.workflow.internal.WorkflowDefinitionChecker.MISSING_DEFINITION_ID;
import static com.codeaffine.workflow.internal.WorkflowDefinitionChecker.MISSING_START_NODE_DECLARATION;
import static com.codeaffine.workflow.internal.WorkflowDefinitionChecker.MISSING_START_NODE_DEFINITION;
import static com.codeaffine.workflow.internal.WorkflowDefinitionChecker.MISSING_SUCCESSOR_DEFINITION;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.START;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;

public class WorkflowDefinitionCheckerTest {

  private WorkflowDefinitionChecker definitionChecker;
  private WorkflowDefinitionImpl definition;

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionImpl();
    definitionChecker = new WorkflowDefinitionChecker();
  }

  @Test
  public void checkDefinition() {
    definition.setId( "id" );
    definition.setStart( START );
    definition.addActivity( START, TestActivity.class, OPERATION_ID );
    definition.addActivity( OPERATION_ID, TestActivity.class, null );

    Throwable actual = captureThrownExceptionOnDefinitionCheck();

    assertThat( actual ).isNull();
  }

  @Test
  public void checkDefinitionWithoutId() {
    Throwable actual = captureThrownExceptionOnDefinitionCheck();

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( MISSING_DEFINITION_ID );
  }

  @Test
  public void checkDefinitionWithoutStartNodeDeclaration() {
    definition.setId( "id" );

    Throwable actual = captureThrownExceptionOnDefinitionCheck();

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( MISSING_START_NODE_DECLARATION );
  }

  @Test
  public void checkDefinitionWithoutStartNodeDefinition() {
    definition.setId( "id" );
    definition.setStart( START );

    Throwable actual = captureThrownExceptionOnDefinitionCheck();

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( MISSING_START_NODE_DEFINITION, START ) );
  }

  @Test
  public void checkDefinitionWithoutSuccessorDefinition() {
    definition.setId( "id" );
    definition.setStart( START );
    definition.addActivity( START, TestActivity.class, OPERATION_ID );

    Throwable actual = captureThrownExceptionOnDefinitionCheck();

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( MISSING_SUCCESSOR_DEFINITION, OPERATION_ID, START ) );
  }

  private Throwable captureThrownExceptionOnDefinitionCheck() {
    return thrownBy( () -> definitionChecker.checkDefinition( definition ) );
  }
}