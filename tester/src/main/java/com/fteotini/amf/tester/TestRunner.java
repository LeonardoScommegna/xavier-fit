package com.fteotini.amf.tester;

import java.lang.reflect.Method;
import java.util.function.Function;

public interface TestRunner {
    TestSuiteOutcome runEntireSuite();

    <T> TestMethodOutcome runSingleMethod(Class<T> clazz, Function<T, Method> methodSelector);
}
