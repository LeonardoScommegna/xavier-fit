package com.fteotini.amf.tester;

public interface TestRunnerFactory {
    TestRunner createTestRunner(TestDiscoveryOptions options);
}
