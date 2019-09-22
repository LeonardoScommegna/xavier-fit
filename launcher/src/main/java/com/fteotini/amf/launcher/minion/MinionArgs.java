package com.fteotini.amf.launcher.minion;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public class MinionArgs implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MinionTestExecutionMode testExecutionMode;
    private final Set<String> classNamePatterns;
    private final MethodUnderTest methodUnderTest;

    private MinionArgs(MinionTestExecutionMode testExecutionMode, Set<String> classNamePatterns, MethodUnderTest methodUnderTest) {
        this.testExecutionMode = testExecutionMode;
        this.classNamePatterns = classNamePatterns;
        this.methodUnderTest = methodUnderTest;
    }

    public static MinionArgs ForSingleMethod(MethodUnderTest methodUnderTest) {
        return new MinionArgs(MinionTestExecutionMode.SINGLE_METHOD, null, methodUnderTest);
    }

    public static MinionArgs ForEntireSuite(Set<String> classNamePatterns) {
        return new MinionArgs(MinionTestExecutionMode.ENTIRE_SUITE, classNamePatterns, null);
    }

    public MinionTestExecutionMode getTestExecutionMode() {
        return testExecutionMode;
    }

    public Optional<Set<String>> getClassNamePatterns() {
        return Optional.ofNullable(classNamePatterns);
    }

    public Optional<MethodUnderTest> getMethodUnderTest() {
        return Optional.ofNullable(methodUnderTest);
    }
}
