package com.fteotini.amf.tester;

import java.nio.file.Path;
import java.util.Set;

public interface TestRunnerFactory {
    TestRunner createTestRunner(Set<Path> additionalClassPaths);
}
