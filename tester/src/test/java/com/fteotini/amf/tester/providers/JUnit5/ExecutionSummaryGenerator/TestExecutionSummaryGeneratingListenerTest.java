package com.fteotini.amf.tester.providers.JUnit5.ExecutionSummaryGenerator;

import com.fteotini.amf.tester.ExecutionSummary.ExecutionResult;
import com.fteotini.amf.tester.ExecutionSummary.TestEntityType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.platform.engine.TestDescriptor.Type;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Tag("UnitTest")
class TestExecutionSummaryGeneratingListenerTest {
    private static final UniqueId EngineId = UniqueId.forEngine("dummy-engine");
    private static final Map<Type, TestEntityType> testTypeMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(Type.TEST, TestEntityType.Method),
            new AbstractMap.SimpleEntry<>(Type.CONTAINER, TestEntityType.Class)
    );
    private static final Map<TestExecutionResult.Status, ExecutionResult> testResultMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(TestExecutionResult.Status.SUCCESSFUL, ExecutionResult.Success),
            new AbstractMap.SimpleEntry<>(TestExecutionResult.Status.FAILED, ExecutionResult.Failure)
    );

    private static Stream<Arguments> availableDescriptorStates() {
        return Stream.of(
                arguments(Type.TEST, TestExecutionResult.successful()),
                arguments(Type.TEST, TestExecutionResult.failed(new RuntimeException())),
                arguments(Type.CONTAINER, TestExecutionResult.failed(new RuntimeException())),
                arguments(Type.CONTAINER, TestExecutionResult.successful())
        );
    }

    private static TestDescriptor buildTestDescriptor(String entityName, Type descriptorType) {
        return buildTestDescriptor(entityName, descriptorType, null);
    }

    private static TestDescriptor buildTestDescriptor(String entityName, Type descriptorType, Function<TestDescriptor, Set<? extends TestDescriptor>> childrenSupplier) {
        var mock = mock(TestDescriptor.class);
        when(mock.getDisplayName()).thenReturn(entityName);
        when(mock.getType()).thenReturn(descriptorType);
        when(mock.getUniqueId()).thenReturn(EngineId.append(descriptorType.toString(), UUID.randomUUID().toString()));

        if (childrenSupplier != null) {
            var children = childrenSupplier.apply(mock);
            doReturn(children).when(mock).getChildren();

            for (var child : children) {
                when(child.getParent()).thenReturn(Optional.of(mock));
            }
        }

        return mock;
    }

    @Test
    void Given_a_not_yet_started_execution_then_calling_generateTestSuiteOutcome_should_throw() {
        var sut = notStartedSut();
        assertThatExceptionOfType(TestSuiteNotFinishedYetException.class)
                .isThrownBy(sut::generateTestSuiteOutcome);
    }

    @Test
    void Given_a_not_yet_finished_execution_then_calling_generateTestSuiteOutcome_should_throw() {
        var sut = startedSut();
        assertThatExceptionOfType(TestSuiteNotFinishedYetException.class)
                .isThrownBy(sut::generateTestSuiteOutcome);
    }

    @Test
    void Given_a_finished_execution_with_no_test_it_should_return_an_empty_outcome() {
        var sut = finishedSut();

        var result = sut.generateTestSuiteOutcome();
        assertThat(result.getTestContainers()).isEmpty();
    }

    @Test
    void Given_a_test_identifier_with_parent_then_it_should_build_the_correct_structure() {
        var child = buildTestDescriptor("newTest", Type.TEST);
        var parent = buildTestDescriptor("testClass", Type.CONTAINER, p -> Collections.singleton(child));
        var parentIdentifier = TestIdentifier.from(parent);
        var childIdentifier = TestIdentifier.from(child);

        var sut = finishedSut(s -> {
            s.executionFinished(childIdentifier, TestExecutionResult.successful());
            s.executionFinished(parentIdentifier, TestExecutionResult.successful());
        });

        var result = sut.generateTestSuiteOutcome();
        assertThat(result.getTestContainers()).hasSize(1);

        var firstLevelChild = result.getTestContainers().stream().findFirst().get();
        assertThat(firstLevelChild.getEntityName()).isEqualTo("testClass");
        assertThat(firstLevelChild.hasChildren()).isTrue();
        assertThat(firstLevelChild.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(firstLevelChild.getException()).isEmpty();
        assertThat(firstLevelChild.getSkipReason()).isEmpty();
        assertThat(firstLevelChild.getType()).isEqualTo(TestEntityType.Class);
        assertThat(firstLevelChild.getChildren()).hasSize(1);

        var secondLevelChild = firstLevelChild.getChildren().stream().findFirst().get();
        assertThat(secondLevelChild.getEntityName()).isEqualTo("newTest");
        assertThat(secondLevelChild.hasChildren()).isFalse();
        assertThat(secondLevelChild.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(secondLevelChild.getException()).isEmpty();
        assertThat(secondLevelChild.getSkipReason()).isEmpty();
        assertThat(secondLevelChild.getType()).isEqualTo(TestEntityType.Method);
        assertThat(secondLevelChild.getChildren()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("availableDescriptorStates")
    void Given_a_test_then_marking_it_as_finished_should_set_the_correct_properties_on_its_node(Type actualType, TestExecutionResult actualResult) {
        var test = TestIdentifier.from(buildTestDescriptor("foo", actualType));

        var sut = finishedSut(s -> s.executionFinished(test, actualResult));

        var result = sut.generateTestSuiteOutcome().getTestContainers().stream().findFirst().get();
        assertThat(result.getEntityName()).isEqualTo("foo");
        assertThat(result.getType()).isEqualTo(testTypeMap.get(actualType));
        assertThat(result.getResult()).isEqualTo(testResultMap.get(actualResult.getStatus()));
    }

    @ParameterizedTest
    @EnumSource(value = Type.class,names = {"CONTAINER","TEST"})
    void Given_a_test_then_marking_it_as_skipped_should_set_the_correct_properties_on_its_node(Type descriptorType) {
        var test = TestIdentifier.from(buildTestDescriptor("foo", descriptorType));

        var sut = finishedSut(s -> s.executionSkipped(test, "Reason"));

        var skippedTest = sut.generateTestSuiteOutcome().getTestContainers().stream().findFirst().get();

        assertThat(skippedTest.getSkipReason()).contains("Reason");
        assertThat(skippedTest.getResult()).isEqualTo(ExecutionResult.Skipped);
        assertThat(skippedTest.getType()).isEqualTo(testTypeMap.get(descriptorType));
    }

    @ParameterizedTest
    @EnumSource(value = Type.class,names = {"CONTAINER","TEST"})
    void Given_a_test_then_marking_it_as_failed_should_set_the_correct_properties_on_its_node(Type descriptorType) {
        var test = TestIdentifier.from(buildTestDescriptor("foo", descriptorType));

        var throwable = new IllegalStateException();
        var sut = finishedSut(s -> s.executionFinished(test, TestExecutionResult.failed(throwable)));

        var skippedTest = sut.generateTestSuiteOutcome().getTestContainers().stream().findFirst().get();
        assertThat(skippedTest.getResult()).isEqualTo(ExecutionResult.Failure);
        assertThat(skippedTest.getType()).isEqualTo(testTypeMap.get(descriptorType));
        assertThat(skippedTest.getException()).contains(throwable);
    }

    private TestExecutionSummaryGeneratingListener notStartedSut() {
        return new TestExecutionSummaryGeneratingListener();
    }

    private TestExecutionSummaryGeneratingListener startedSut() {
        var sut = notStartedSut();
        sut.testPlanExecutionStarted(TestPlan.from(Collections.emptyList()));
        return sut;
    }

    private TestExecutionSummaryGeneratingListener finishedSut(Consumer<TestExecutionSummaryGeneratingListener> execution) {
        var sut = startedSut();
        execution.accept(sut);
        sut.testPlanExecutionFinished(TestPlan.from(Collections.emptyList()));

        return sut;
    }

    private TestExecutionSummaryGeneratingListener finishedSut() {
        return finishedSut(noop -> {
        });
    }
}

