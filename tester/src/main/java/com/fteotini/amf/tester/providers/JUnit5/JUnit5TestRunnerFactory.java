package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.tester.TestDiscoveryOptions;
import com.fteotini.amf.tester.TestRunner;
import com.fteotini.amf.tester.TestRunnerFactory;

public class JUnit5TestRunnerFactory implements TestRunnerFactory {

    @Override
    public TestRunner createTestRunner(TestDiscoveryOptions options) {
        return new JUnit5TestRunner(new DiscoveryRequestBuilder(options), new ContextualTestRunner(options.getAdditionalClassPaths()));
    }
}
