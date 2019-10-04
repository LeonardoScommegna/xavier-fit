package com.fteotini.amf.launcher.minion;

import com.fteotini.amf.commons.tester.MethodUnderTest;
import com.fteotini.amf.commons.tester.TestExecutionMode;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public class MinionArgs implements Serializable {
    private static final long serialVersionUID = 1L;

    private final TestExecutionMode testExecutionMode;
    private final MethodUnderTest methodUnderTest;

    private final Set<String> classNamePatterns;
    private final Set<String> packageNames;

    private MinionArgs(TestExecutionMode testExecutionMode, Set<String> classNamePatterns, Set<String> packageNames, MethodUnderTest methodUnderTest) {
        this.testExecutionMode = testExecutionMode;
        this.classNamePatterns = classNamePatterns;
        this.packageNames = packageNames;
        this.methodUnderTest = methodUnderTest;
    }

    public static MinionArgs ForSingleMethod(MethodUnderTest methodUnderTest) {
        return new MinionArgs(TestExecutionMode.SINGLE_METHOD, null, null, methodUnderTest);
    }

    public static MinionArgs ForEntireSuite(Set<String> classNamePatterns, Set<String> packageNames) {
        return new MinionArgs(TestExecutionMode.ENTIRE_SUITE, classNamePatterns, packageNames, null);
    }

    public static MinionArgs ForEntireSuite() {
        return new MinionArgs(TestExecutionMode.ENTIRE_SUITE, null, null, null);
    }

    TestExecutionMode getTestExecutionMode() {
        return testExecutionMode;
    }

    Optional<Set<String>> getClassNamePatterns() {
        return Optional.ofNullable(classNamePatterns);
    }

    Optional<MethodUnderTest> getMethodUnderTest() {
        return Optional.ofNullable(methodUnderTest);
    }

    Optional<Set<String>> getPackageNames() {
        return Optional.ofNullable(packageNames);
    }
}
