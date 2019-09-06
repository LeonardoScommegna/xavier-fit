package com.fteotini.amf.tester.outcomes;


import java.util.Set;

public class TestClassOutcome<T extends Class<?>> extends TestOutcomeBase<T> {
    private final Set<TestMethodOutcome> children;

    TestClassOutcome(T testEntity, Set<TestMethodOutcome> children, TestResult outcome, String skipReason, Throwable exception) {
        super(testEntity, outcome, skipReason, exception);
        this.children = children;
    }

    public Set<TestMethodOutcome> getTestMethods() {
        return children;
    }

    public T getTestClass() {
        return getTestEntity();
    }
}
