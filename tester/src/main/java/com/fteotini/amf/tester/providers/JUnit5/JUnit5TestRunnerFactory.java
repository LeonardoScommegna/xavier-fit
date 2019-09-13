package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.tester.TestRunner;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public class JUnit5TestRunnerFactory {
    public TestRunner createTestRunner(Set<Path> additionalClassPaths) {
        return new JUnit5TestRunner(new DiscoveryRequestBuilder(DiscoveryRequestOptions.ForEntireSuite(Collections.emptySet(), additionalClassPaths)), new ContextualTestRunner(additionalClassPaths));
    }
}
