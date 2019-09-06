package com.fteotini.amf.tester;

import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;

import java.lang.reflect.Method;
import java.util.function.Function;

public interface TestRunner {
    TestSuiteOutcome runEntireSuite();

    <T> TestSuiteOutcome runSingleMethod(Class<T> clazz, Function<T, Method> methodSelector);
}
