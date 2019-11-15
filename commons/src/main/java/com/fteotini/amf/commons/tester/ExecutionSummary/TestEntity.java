package com.fteotini.amf.commons.tester.ExecutionSummary;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the outcome of a test in a serializable way.
 *
 * This class contains information like the result of the test, its exception if failed, its skip reason if skipped and
 * its {@link TestEntityType}.
 *
 * If it is a Class, then it contains a reference to a set of child {@link TestEntity}.
 *
 * @author Federico Teotini (teotini.federico@gmail.com)
 */
public final class TestEntity implements Serializable {
    private static final long serialVersionUID = 42L;

    private final String entityName;
    private final TestEntityType type;
    private final ExecutionResult result;
    private final String skipReason;
    private final Throwable exception;
    private final Set<TestEntity> children;

    private TestEntity(String entityName, TestEntityType type, ExecutionResult result, String skipReason, Throwable exception, Set<TestEntity> children) {
        this.entityName = entityName;
        this.type = type;
        this.result = result;
        this.skipReason = skipReason;
        this.exception = exception;
        this.children = children != null ? Set.copyOf(children) : Collections.emptySet();
    }

    public static TestEntity Success(String entityName, TestEntityType type, Set<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Success,null, null, children);
    }

    public static TestEntity Failure(String entityName, TestEntityType type, Throwable exception, Set<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Failure,null, exception, children);
    }

    public static TestEntity Skipped(String entityName, TestEntityType type, String skipReason, Set<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Skipped,skipReason, null, children);
    }

    public String getEntityName() {
        return entityName;
    }

    public TestEntityType getType() {
        return type;
    }

    public Set<TestEntity> getChildren() {
        return children;
    }

    public ExecutionResult getResult() {
        return result;
    }

    public Optional<String> getSkipReason() {
        return Optional.ofNullable(skipReason);
    }

    public Optional<Throwable> getException() {
        return Optional.ofNullable(exception);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
