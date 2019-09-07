package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.ExecutionResult;
import com.fteotini.amf.tester.outcomes.TestEntity;
import com.fteotini.amf.tester.outcomes.TestEntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class TestSuiteOutcomeBuilderTest {

    private TestSuiteOutcomeBuilder sut;

    @BeforeEach
    void setUp() {
        sut = new TestSuiteOutcomeBuilder();
    }

    @Test
    void Given_a_childless_root_node_it_should_generate_an_empty_result() {
        var root = TestNode.Root();

        var result = sut.build(root);

        assertThat(result.getRootTestContainers()).isEmpty();
    }

    @Test
    void Given_a_first_level_child_it_should_build_the_correct_TestEntity() {
        var entityName = "foo";
        TestNode testNode = buildTestClassNode(entityName);

        var root = TestNode.Root().addChild(testNode);

        var result = sut.build(root).getRootTestContainers();
        assertThat(result).hasSize(1);

        var actualEntity = result.stream().findFirst().get();
        assertThat(actualEntity).usingRecursiveComparison().isEqualTo(TestEntity.Success(entityName, TestEntityType.Class));
    }

    private static Stream<Arguments> testNode_EntityMap() {
        return Stream.of(
                arguments(
                        buildTestNode("name", TestDescriptor.Type.TEST,TestExecutionResult.successful(), null),
                        buildTestEntity("name",TestEntityType.Method,ExecutionResult.Success,null, null)
                )
        );
    }

    private static TestEntity buildTestEntity(String entityName, TestEntityType type, ExecutionResult result, String skipReason, Throwable exception) {
        TestEntity entity;
        switch (result) {
            case Success:
                entity = TestEntity.Success(entityName,type);
                break;
            case Failure:
                entity = TestEntity.Failure(entityName,type,exception);
                break;
            case Skipped:
                entity = TestEntity.Skipped(entityName,type,skipReason);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        };

        return entity;
    }

    private static TestNode buildTestNode(String entityName, TestDescriptor.Type descriptorType, TestExecutionResult executionResult, String skipReason) {
        var mock = mock(TestDescriptor.class);
        when(mock.getDisplayName()).thenReturn(entityName);
        when(mock.getType()).thenReturn(descriptorType);
        when(mock.getUniqueId()).thenReturn(UniqueId.root("dummy","type"));

        var node = new TestNode(TestIdentifier.from(mock));

        if (executionResult != null)
            node.setResult(executionResult);
        if (skipReason != null)
            node.setSkipReason(skipReason);

        return node;
    }
}