package com.codeaffine.workflow.persistence;

import com.codeaffine.workflow.NodeDefinition;

public class OperationPointerMemento {

  private final NodeDefinition currentNode;
  private final boolean initialized;
  private final boolean acquired;

  public OperationPointerMemento( NodeDefinition currentNode, boolean initialized, boolean acquired ) {
    this.currentNode = currentNode;
    this.initialized = initialized;
    this.acquired = acquired;
  }

  public NodeDefinition getCurrentNode() {
    return currentNode;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public boolean isAcquired() {
    return acquired;
  }
}