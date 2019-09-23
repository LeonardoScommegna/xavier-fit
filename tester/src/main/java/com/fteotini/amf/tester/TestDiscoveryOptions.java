package com.fteotini.amf.tester;

import com.fteotini.amf.commons.tester.MethodUnderTest;
import com.fteotini.amf.commons.tester.TestExecutionMode;

import java.nio.file.Path;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

public class TestDiscoveryOptions {
    private static final String DEFAULT_CLASS_PATTERNS = "^.*Tests?$";
    private final TestExecutionMode testExecutionMode;

    //selectors
    private Set<Path> additionalClassPaths = emptySet();
    private Set<MethodUnderTest> selectedMethods = emptySet();

    // filters
    private Set<String> includedClassNamePatterns = singleton(DEFAULT_CLASS_PATTERNS);
    private Set<String> excludedClassNamePatterns = emptySet();
    private Set<String> includedPackageNames = emptySet();
    private Set<String> excludedPackageNames = emptySet();

    public TestDiscoveryOptions(TestExecutionMode testExecutionMode) {
        this.testExecutionMode = testExecutionMode;
    }

    public TestDiscoveryOptions withAdditionalClassPaths(Set<Path> additionalClassPaths) {
        this.additionalClassPaths = additionalClassPaths;
        return this;
    }

    public TestDiscoveryOptions withSelectedMethods(Set<MethodUnderTest> selectedMethods) {
        if (testExecutionMode == TestExecutionMode.ENTIRE_SUITE) {
            throw new IllegalStateException("Setting methods when TestExecutionMode is ENTIRE_SUITE is forbidden");
        }
        this.selectedMethods = selectedMethods;
        return this;
    }

    public TestDiscoveryOptions withIncludedClassNamePatterns(Set<String> includedClassNamePatterns) {
        this.includedClassNamePatterns = includedClassNamePatterns;
        return this;
    }

    public TestDiscoveryOptions withExcludedClassNamePatterns(Set<String> excludedClassNamePatterns) {
        this.excludedClassNamePatterns = excludedClassNamePatterns;
        return this;
    }

    public TestDiscoveryOptions withIncludedPackageNames(Set<String> includedPackageNames) {
        this.includedPackageNames = includedPackageNames;
        return this;
    }

    public TestDiscoveryOptions withExcludedPackageNames(Set<String> excludedPackageNames) {
        this.excludedPackageNames = excludedPackageNames;
        return this;
    }

    public Set<String> getIncludedClassNamePatterns() {
        return includedClassNamePatterns;
    }

    public Set<String> getExcludedClassNamePatterns() {
        return excludedClassNamePatterns;
    }

    public Set<String> getIncludedPackageNames() {
        return includedPackageNames;
    }

    public TestExecutionMode getTestExecutionMode() {
        return testExecutionMode;
    }

    public Set<String> getExcludedPackageNames() {
        return excludedPackageNames;
    }

    public Set<Path> getAdditionalClassPaths() {
        return additionalClassPaths;
    }

    public Set<MethodUnderTest> getSelectedMethods() {
        return selectedMethods;
    }
}
