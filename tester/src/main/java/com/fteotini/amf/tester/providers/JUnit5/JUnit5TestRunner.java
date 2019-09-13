package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.tester.ExecutionSummary.TestExecutionSummary;
import com.fteotini.amf.tester.TestRunner;
import com.fteotini.amf.tester.providers.JUnit5.ExecutionSummaryGenerator.TestExecutionSummaryGeneratingListener;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

class JUnit5TestRunner implements TestRunner {
    private final DiscoveryRequestBuilder requestBuilder;
    private final ContextualTestRunner contextualTestRunner;
    private final Launcher launcher;

    JUnit5TestRunner(DiscoveryRequestBuilder requestBuilder, ContextualTestRunner contextualTestRunner, Supplier<Launcher> launcherFactory) {
        this.requestBuilder = requestBuilder;
        this.contextualTestRunner = contextualTestRunner;
        launcher = launcherFactory.get();
    }

    JUnit5TestRunner(DiscoveryRequestBuilder requestBuilder, ContextualTestRunner contextualTestRunner) {
        this(requestBuilder, contextualTestRunner, LauncherFactory::create);
    }

    @Override
    public TestExecutionSummary runEntireSuite() {
        return contextualTestRunner.run(this::invokeTestSuite);
    }

    private TestExecutionSummary invokeTestSuite() {
        var testExecutionSummaryGeneratingListener = makeListener();
        launcher.registerTestExecutionListeners(testExecutionSummaryGeneratingListener);

        var request = requestBuilder.build();
        launcher.execute(request);

        return testExecutionSummaryGeneratingListener.generateTestSuiteOutcome();
    }

    TestExecutionSummaryGeneratingListener makeListener() {
        return new TestExecutionSummaryGeneratingListener();
    }

    @Override
    public <T> TestExecutionSummary runSingleMethod(Class<T> clazz, Function<T, Method> methodSelector) {
        return null;
    }
}
