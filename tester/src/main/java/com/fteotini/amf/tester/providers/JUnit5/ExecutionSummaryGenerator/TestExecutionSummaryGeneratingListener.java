package com.fteotini.amf.tester.providers.JUnit5.ExecutionSummaryGenerator;

import com.fteotini.amf.commons.tester.ExecutionSummary.TestEntity;
import com.fteotini.amf.commons.tester.ExecutionSummary.TestEntityType;
import com.fteotini.amf.commons.tester.ExecutionSummary.TestExecutionSummary;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.platform.engine.TestDescriptor.Type;

public class TestExecutionSummaryGeneratingListener implements TestExecutionListener {
    private static final String ROOT_IDENTIFIER = "Root_Identifier";
    private static final Map<Type, TestEntityType> typeMapping = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Type.TEST, TestEntityType.Method),
            new AbstractMap.SimpleEntry<>(Type.CONTAINER, TestEntityType.Class)
    );

    private final Map<String, Set<String>> testIdsByParentId = new ConcurrentHashMap<>();
    private final Map<String, TestEntity> testsEntitiesByUniqueId = new ConcurrentHashMap<>();
    private boolean isFinished = false;

    public TestExecutionSummary generateTestSuiteOutcome() {
        if (!isFinished)
            throw new TestSuiteNotFinishedYetException();

        var set = getChildrenByParentId(ROOT_IDENTIFIER);

        return new TestExecutionSummary(set);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        isFinished = true;
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        var entity = buildSkippedEntity(testIdentifier, reason);
        addEntity(testIdentifier, entity);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!isEngine(testIdentifier)) {
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
    }

    private static boolean isEngine(TestIdentifier testIdentifier) {
        return testIdentifier.isContainer() && testIdentifier.getParentId().isEmpty() && isEngineId(testIdentifier.getUniqueId());
    }

    private static boolean isEngineId(String uniqueId) {
        var segments = uniqueId.split("\\/");
        return segments.length == 1 && segments[0].matches("^\\[engine:.+?\\]$");
    }

    private TestEntity buildFailedEntity(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Failure(testIdentifier.getDisplayName(), typeMapping.get(testIdentifier.getType()), testExecutionResult.getThrowable().get(), children);
    }

    private Set<TestEntity> getChildrenByParentId(String parentId) {
        var childrenIds = testIdsByParentId.getOrDefault(parentId, Collections.emptySet());

        return childrenIds.stream().map(testsEntitiesByUniqueId::get).collect(Collectors.toSet());
    }

    private TestEntity buildSuccessfulEntity(TestIdentifier testIdentifier) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Success(testIdentifier.getDisplayName(), typeMapping.get(testIdentifier.getType()), children);
    }

    private TestEntity buildSkippedEntity(TestIdentifier testIdentifier, String reason) {
        var children = getChildrenByParentId(testIdentifier.getUniqueId());
        return TestEntity.Skipped(testIdentifier.getDisplayName(), typeMapping.get(testIdentifier.getType()), reason, children);
    }

    private void addEntity(TestIdentifier testIdentifier, TestEntity entity) {
        var entityId = testIdentifier.getUniqueId();
        testsEntitiesByUniqueId.put(entityId, entity);

        var parentId = testIdentifier.getParentId().filter(id -> !isEngineId(id)).orElse(ROOT_IDENTIFIER);
        if (!testIdsByParentId.containsKey(parentId)) {
            testIdsByParentId.put(parentId, new HashSet<>());
        }

        testIdsByParentId.get(parentId).add(entityId);
    }
}
