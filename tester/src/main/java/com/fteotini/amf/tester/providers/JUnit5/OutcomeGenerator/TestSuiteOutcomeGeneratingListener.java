package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.TestEntity;
import com.fteotini.amf.tester.outcomes.TestEntityType;
import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotFinishedYetException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.platform.engine.TestDescriptor.Type;

public class TestSuiteOutcomeGeneratingListener implements TestExecutionListener {
    private static final String ROOT_IDENTIFIER = "Root_Identifier";
    private static final Map<Type, TestEntityType> typeMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Type.TEST, TestEntityType.Method),
            new AbstractMap.SimpleEntry<>(Type.CONTAINER, TestEntityType.Class)
    );

    private final Map<String, Set<String>> testIdsByParentId = new ConcurrentHashMap<>();
    private final Map<String, TestEntity> testsEntitiesByUniqueId = new ConcurrentHashMap<>();
    private boolean isFinished = false;


    public TestSuiteOutcome generateTestSuiteOutcome() {
        if (!isFinished)
            throw new TestSuiteNotFinishedYetException();

        var set = getChildrenByParentId(ROOT_IDENTIFIER);

        return new TestSuiteOutcome(set);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        isFinished = true;
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        TestEntity entity;
        switch (testExecutionResult.getStatus()) {
            case SUCCESSFUL:
                entity = buildSuccessfulEntity(testIdentifier);
                break;
            case FAILED:
                entity = buildFailedEntity(testIdentifier, testExecutionResult);
                break;
            case ABORTED:
            default:
                throw new IllegalStateException("Unexpected value: " + testExecutionResult.getStatus());
        }
        addEntity(testIdentifier, entity);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        var entity = buildSkippedEntity(testIdentifier, reason);
        addEntity(testIdentifier, entity);
    }

    private void addEntity(TestIdentifier testIdentifier, TestEntity entity) {
        var entityId = testIdentifier.getUniqueId();
        testsEntitiesByUniqueId.put(entityId, entity);

        var parentId = testIdentifier.getParentId().orElse(ROOT_IDENTIFIER);
        if (!testIdsByParentId.containsKey(parentId)) {
            testIdsByParentId.put(parentId, Set.of(entityId));
        } else {
            testIdsByParentId.get(parentId).add(entityId);
        }
    }

    private TestEntity buildFailedEntity(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Failure(testIdentifier.getDisplayName(),typeMapping.get(testIdentifier.getType()), testExecutionResult.getThrowable().get(), children);
    }

    private Set<TestEntity> getChildrenByParentId(String parentId) {
        var childrenIds = testIdsByParentId.getOrDefault(parentId, Collections.emptySet());

        return childrenIds.stream().map(testsEntitiesByUniqueId::get).collect(Collectors.toSet());
    }

    private TestEntity buildSuccessfulEntity(TestIdentifier testIdentifier) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Success(testIdentifier.getDisplayName(), typeMapping.get(testIdentifier.getType()),children);
    }

    private TestEntity buildSkippedEntity(TestIdentifier testIdentifier, String reason) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Skipped(testIdentifier.getDisplayName(), typeMapping.get(testIdentifier.getType()), reason,children);
    }
}
