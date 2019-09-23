package com.fteotini.amf.tester;


import com.fteotini.amf.commons.tester.ExecutionSummary.TestExecutionSummary;

import java.lang.reflect.Method;
import java.util.function.Function;

public interface TestRunner {
    TestExecutionSummary runEntireSuite();

    <T> TestExecutionSummary runSingleMethod(Class<T> clazz, Function<T, Method> methodSelector);
}
