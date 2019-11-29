package com.fteotini.Xavier.tester.providers.JUnit5;

import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestExecutionSummary;
import com.fteotini.Xavier.tester.TestRunner;
import com.fteotini.Xavier.tester.providers.JUnit5.ExecutionSummaryGenerator.TestExecutionSummaryGeneratingListener;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;

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
    public TestExecutionSummary run() {
        return contextualTestRunner.run(this::invokeTests);
    }

    private TestExecutionSummary invokeTests() {
        var testExecutionSummaryGeneratingListener = makeListener();
        launcher.registerTestExecutionListeners(testExecutionSummaryGeneratingListener);

        var request = requestBuilder.build();
        launcher.execute(request);

        return testExecutionSummaryGeneratingListener.generateTestSuiteOutcome();
    }

    TestExecutionSummaryGeneratingListener makeListener() {
        return new TestExecutionSummaryGeneratingListener();
    }
}
