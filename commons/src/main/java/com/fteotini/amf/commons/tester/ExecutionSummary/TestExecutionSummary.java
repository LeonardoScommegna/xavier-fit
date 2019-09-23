package com.fteotini.amf.commons.tester.ExecutionSummary;

import java.io.Serializable;
import java.util.Set;

public class TestExecutionSummary implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Set<TestEntity> testContainers;

    public TestExecutionSummary(Set<TestEntity> testContainers) {
        this.testContainers = testContainers;
    }

    public Set<TestEntity> getTestContainers() {
        return testContainers;
    }
}
