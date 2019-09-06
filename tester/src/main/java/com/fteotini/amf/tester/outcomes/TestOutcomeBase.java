package com.fteotini.amf.tester.outcomes;

import java.util.Optional;

abstract class TestOutcomeBase<T> {
    private final T testEntity;
    private final TestResult outcome;
    private final String skipReason;
    private final Throwable exception;

    TestOutcomeBase(T testEntity, TestResult outcome, String skipReason, Throwable exception) {
        this.testEntity = testEntity;
        this.outcome = outcome;
        this.skipReason = skipReason;
        this.exception = exception;
    }

    public TestResult getOutcome() {
        return outcome;
    }

    public Optional<String> getSkipReason() {
        return Optional.ofNullable(skipReason);
    }

    public Optional<Throwable> getException() {
        return Optional.ofNullable(exception);
    }

    T getTestEntity(){ return testEntity; }
}
