package com.fteotini.amf.tester.outcomes;

import java.lang.reflect.Method;

public class TestMethodOutcome extends TestOutcomeBase<Method> {

    private TestMethodOutcome(Method testEntity, TestResult outcome, String skipReason, Throwable exception) {
        super(testEntity, outcome, skipReason, exception);
    }

    public static TestMethodOutcome Success(Method testMethod){
        return new TestMethodOutcome(testMethod,TestResult.Success, null, null);
    }

    public static TestMethodOutcome Failure(Method testMethod, Throwable exception){
        return new TestMethodOutcome(testMethod,TestResult.Failure, null, exception);
    }

    public static TestMethodOutcome Skipped(Method testMethod, String skipReason){
        return new TestMethodOutcome(testMethod,TestResult.Skipped, skipReason, null);
    }

    public Method getTestMethod() {
        return getTestEntity();
    }
}
