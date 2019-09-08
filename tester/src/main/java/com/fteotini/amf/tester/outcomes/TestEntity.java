package com.fteotini.amf.tester.outcomes;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class TestEntity {
    private final String entityName;
    private final TestEntityType type;
    private final ExecutionResult result;
    private final String skipReason;
    private final Throwable exception;
    private final Collection<TestEntity> children;

    private TestEntity(String entityName, TestEntityType type, ExecutionResult result, String skipReason, Throwable exception, Collection<TestEntity> children) {
        this.entityName = entityName;
        this.type = type;
        this.result = result;
        this.skipReason = skipReason;
        this.exception = exception;
        this.children = children != null ? Collections.unmodifiableCollection(children) : Collections.unmodifiableCollection(Collections.emptyList());
    }

    public static TestEntity Success(String entityName, TestEntityType type) {
        return TestEntity.Success(entityName,type,null);
    }

    public static TestEntity Success(String entityName, TestEntityType type, Collection<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Success,null, null, children);
    }

    public static TestEntity Failure(String entityName, TestEntityType type, Throwable exception) {
        return TestEntity.Failure(entityName,type,exception, null);
    }

    public static TestEntity Failure(String entityName, TestEntityType type, Throwable exception, Collection<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Failure,null, exception, children);
    }

    public static TestEntity Skipped(String entityName, TestEntityType type, String skipReason) {
        return TestEntity.Skipped(entityName,type,skipReason,null);
    }

    public static TestEntity Skipped(String entityName, TestEntityType type, String skipReason, Collection<TestEntity> children) {
        return new TestEntity(entityName,type,ExecutionResult.Skipped,skipReason, null, children);
    }

    public String getEntityName() {
        return entityName;
    }

    public TestEntityType getType() {
        return type;
    }

    public Collection<TestEntity> getChildren() {
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
