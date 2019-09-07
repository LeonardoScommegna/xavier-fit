package com.fteotini.amf.tester.outcomes;

import java.util.Set;

public class TestSuiteOutcome {
    private final Set<TestEntity> rootTestContainers;

    public TestSuiteOutcome(Set<TestEntity> rootTestContainers) {
        this.rootTestContainers = rootTestContainers;
    }

    public Set<TestEntity> getRootTestContainers() {
        return rootTestContainers;
    }
}
