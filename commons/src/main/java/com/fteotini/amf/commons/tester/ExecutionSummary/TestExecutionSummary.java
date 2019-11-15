package com.fteotini.amf.commons.tester.ExecutionSummary;

import java.io.Serializable;
import java.util.Set;

/**
 * Contains the results of a test run
 *
 * @author Federico Teotini (teotini.federico@gmail.com)
 */
public class TestExecutionSummary implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Set<TestEntity> testContainers;

    public TestExecutionSummary(Set<TestEntity> testContainers) {
        this.testContainers = testContainers;
    }

    /**
     * The roots (most likely test classes) of what tests were run.
     *
     * @return A set of roots {@link TestEntity}
     */
    public Set<TestEntity> getTestContainers() {
        return testContainers;
    }
}
