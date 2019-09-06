package com.fteotini.amf.tester.outcomes;

import java.util.Set;

public class TestSuiteOutcome {
    private final Set<TestClassOutcome<?>> testClasses;
    private final TestResult outcome;

    public TestSuiteOutcome(Set<TestClassOutcome<?>> testClasses, TestResult outcome) {
        this.testClasses = testClasses;
        this.outcome = outcome;
    }

    public Set<TestClassOutcome<?>> getTestClasses() {
        return testClasses;
    }

    public TestResult getOutcome() {
        return outcome;
    }
}
