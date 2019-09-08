package com.fteotini.amf.tester.ExecutionSummary;

import java.util.Set;

public class TestExecutionSummary {
    private final Set<TestEntity> testContainers;

    public TestExecutionSummary(Set<TestEntity> testContainers) {
        this.testContainers = testContainers;
    }

    public Set<TestEntity> getTestContainers() {
        return testContainers;
    }
}
