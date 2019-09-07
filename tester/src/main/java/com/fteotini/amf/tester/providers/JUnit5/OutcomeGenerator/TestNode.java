package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class TestNode {
    final Queue<TestNode> children = new ConcurrentLinkedQueue<>();
    private final boolean isRoot;

    private String skipReason;
    private final TestIdentifier identifier;
    private TestExecutionResult result;

    private TestNode() {
        identifier = null;
        isRoot = true;
    }

    TestNode(TestIdentifier identifier) {
        this.identifier = identifier;
        isRoot = false;
    }

    static TestNode Root() {
        return new TestNode();
    }

    void addChild(TestNode child){
        children.add(child);
    }

    Optional<TestExecutionResult> getResult() {
        return Optional.ofNullable(result);
    }

    void setResult(TestExecutionResult result) {
        this.result = result;
    }

    Optional<TestIdentifier> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    Optional<String> getSkipReason() {
        return Optional.ofNullable(skipReason);
    }

    void setSkipReason(String skipReason) {
        this.skipReason = skipReason;
    }

    boolean isRoot() {
        return isRoot;
    }
}
