package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestIdentifier;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class TestNode {
    final Queue<TestNode> children = new ConcurrentLinkedQueue<>();

    private final String skipReason;
    private final TestIdentifier identifier;
    private TestExecutionResult result;

    TestNode(TestIdentifier identifier) {
        this(identifier,null);
    }

    TestNode(TestIdentifier identifier, String skipReason) {
        this.skipReason = skipReason;
        this.identifier = identifier;
    }

    public void addChild(TestNode child){
        children.add(child);
    }

    public Optional<TestExecutionResult> getResult() {
        return Optional.ofNullable(result);
    }

    public void setResult(TestExecutionResult result) {
        this.result = result;
    }

    public Optional<TestIdentifier> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    public Optional<String> getSkipReason() {
        return Optional.ofNullable(skipReason);
    }
}
