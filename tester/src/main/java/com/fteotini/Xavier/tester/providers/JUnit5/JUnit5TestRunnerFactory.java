package com.fteotini.Xavier.tester.providers.JUnit5;

import com.fteotini.Xavier.tester.TestDiscoveryOptions;
import com.fteotini.Xavier.tester.TestRunner;
import com.fteotini.Xavier.tester.TestRunnerFactory;

public class JUnit5TestRunnerFactory implements TestRunnerFactory {

    @Override
    public TestRunner createTestRunner(TestDiscoveryOptions options) {
        return new JUnit5TestRunner(new DiscoveryRequestBuilder(options), new ContextualTestRunner(options.getAdditionalClassPaths()));
    }
}
