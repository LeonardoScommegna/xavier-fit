package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotFinishedYetException;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotStartedYetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.ClassTestDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TestSuiteOutcomeGeneratingListenerTest {
    private final UniqueId EngineId = UniqueId.forEngine("dummy-engine");

    @Mock
    private JupiterConfiguration dummyConfig;
    @Mock
    private TestSuiteOutcomeBuilder builder;
    @Captor
    private ArgumentCaptor<TestNode> rootTestNodeCaptor;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        when(dummyConfig.getDefaultDisplayNameGenerator()).thenReturn(new DisplayNameGenerator.Standard());
    }

    @Test
    void Given_a_not_yet_started_execution_then_calling_generateTestSuiteOutcome_should_throw() {
        var sut = notStartedSut();
        assertThatExceptionOfType(TestSuiteNotStartedYetException.class)
                .isThrownBy(sut::generateTestSuiteOutcome);
    }

    @Test
    void Given_a_not_yet_finished_execution_then_calling_generateTestSuiteOutcome_should_throw() {
        var sut = startedSut();
        assertThatExceptionOfType(TestSuiteNotFinishedYetException.class)
                .isThrownBy(sut::generateTestSuiteOutcome);
    }

    @Test
    void Given_a_not_yet_started_execution_then_calling_testPlanExecutionFinished_should_throw() {
        var dummyTestPlan = TestPlan.from(Collections.emptyList());
        var sut = notStartedSut();

        assertThatExceptionOfType(TestSuiteNotStartedYetException.class)
                .isThrownBy(() -> sut.testPlanExecutionFinished(dummyTestPlan));
    }

    @Test
    void Given_a_finished_execution_then_it_should_return_the_builder_result() {
        var expected = mock(TestSuiteOutcome.class);
        when(builder.build(any())).thenReturn(expected);

        var result = finishedSut().generateTestSuiteOutcome();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void Given_a_finished_execution_with_no_test_it_should_call_the_builder_with_a_no_children_root() {
        var sut = finishedSut();

        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());

        var rootNode = rootTestNodeCaptor.getValue();
        assertThat(rootNode.children).isEmpty();
        assertThat(rootNode.isRoot()).isTrue();
    }

    @Test
    void Given_a_test_identifier_without_parent_then_its_representation_should_be_appended_to_the_root() throws Exception {
        var testIdentifier = TestIdentifier.from(dummyTestMethodDescriptor());
        var sut = finishedSut(s -> s.executionStarted(testIdentifier));

        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());

        var rootNode = rootTestNodeCaptor.getValue();
        assertThat(rootNode.children).hasSize(1);
        assertThat(rootNode.children.peek())
                .usingRecursiveComparison()
                .isEqualTo(new TestNode(testIdentifier));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void Given_a_test_identifier_with_parent_then_it_should_build_the_correct_structure() throws NoSuchMethodException {
        var parent = dummyTestClassDescriptor();
        var parentIdentifier = TestIdentifier.from(parent);
        var child = dummyTestMethodDescriptor(parent);
        var childIdentifier = TestIdentifier.from(child);

        var sut = finishedSut(s -> {
            s.executionStarted(parentIdentifier);
            s.executionStarted(childIdentifier);
        });

        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());

        var rootNode = rootTestNodeCaptor.getValue();
        assertThat(rootNode.children).hasSize(1);

        var firstLevelChild = rootNode.children.peek();
        assertThat(firstLevelChild.getIdentifier()).contains(parentIdentifier);
        assertThat(firstLevelChild.children).hasSize(1);

        var secondLevelChild = firstLevelChild.children.peek();
        assertThat(secondLevelChild.getIdentifier()).contains(childIdentifier);
        assertThat(secondLevelChild.children).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("availableTestResults")
    void Given_a_test_then_marking_it_as_finished_should_edit_the_corresponding_node(TestExecutionResult result) throws NoSuchMethodException {
        var test = TestIdentifier.from(dummyTestMethodDescriptor());

        var sut = finishedSut(s -> {
            s.executionStarted(test);
            s.executionFinished(test, result);
        });
        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());
        //noinspection ConstantConditions
        assertThat(rootTestNodeCaptor.getValue().children.peek().getResult()).contains(result);
    }

    @Test
    void Given_a_test_then_marking_it_as_skipped_should_set_the_correct_properties_on_its_node() throws NoSuchMethodException {
        var test = TestIdentifier.from(dummyTestMethodDescriptor());

        var sut = finishedSut(s -> {
            s.executionStarted(test);
            s.executionSkipped(test, "Reason");
        });
        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());

        var children = rootTestNodeCaptor.getValue().children;
        assertThat(children).hasSize(1);
        //noinspection ConstantConditions
        assertThat(children.peek().getSkipReason()).contains("Reason");
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void Given_a_not_started_test_then_calling_executionSkipped_should_create_the_node() throws NoSuchMethodException {
        var test = TestIdentifier.from(dummyTestMethodDescriptor());

        var sut = finishedSut(s -> {
            s.executionSkipped(test, "Reason");
        });
        sut.generateTestSuiteOutcome();

        verify(builder).build(rootTestNodeCaptor.capture());

        var children = rootTestNodeCaptor.getValue().children;
        assertThat(children).hasSize(1);

        var skippedTest = children.peek();
        assertThat(skippedTest.getSkipReason()).contains("Reason");
        assertThat(skippedTest.getIdentifier()).contains(test);
    }

    private static Collection<TestExecutionResult> availableTestResults() {
        return Set.of(TestExecutionResult.successful(),TestExecutionResult.aborted(new RuntimeException()));
    }

    private TestSuiteOutcomeGeneratingListener notStartedSut() {
        return new TestSuiteOutcomeGeneratingListener(builder);
    }

    private TestSuiteOutcomeGeneratingListener startedSut() {
        var sut = notStartedSut();
        sut.testPlanExecutionStarted(TestPlan.from(Collections.emptyList()));
        return sut;
    }

    private TestSuiteOutcomeGeneratingListener finishedSut(Consumer<TestSuiteOutcomeGeneratingListener> execution) {
        var sut = startedSut();

        execution.accept(sut);

        sut.testPlanExecutionFinished(TestPlan.from(Collections.emptyList()));

        return sut;
    }

    private TestSuiteOutcomeGeneratingListener finishedSut() {
        return finishedSut(noop -> {
        });
    }

    private TestDescriptor dummyTestClassDescriptor() {
        return new ClassTestDescriptor(
                EngineId.append("class", DummyTestClass.class.getName()),
                DummyTestClass.class,
                dummyConfig
        );
    }

    private TestDescriptor dummyTestMethodDescriptor() throws NoSuchMethodException {
        return dummyTestMethodDescriptor(null);
    }

    private TestDescriptor dummyTestMethodDescriptor(TestDescriptor parent) throws NoSuchMethodException {
        var parentID = parent != null ? parent.getUniqueId() : EngineId;
        var descriptor = new TestMethodTestDescriptor(
                parentID.append("method", "dummyTestMethod"),
                DummyTestClass.class,
                DummyTestClass.class.getDeclaredMethod("dummyTestMethod"),
                dummyConfig
        );

        if (parent != null) {
            parent.addChild(descriptor);
        }

        return descriptor;
    }

    static class DummyTestClass {
        void dummyTestMethod() {
        }
    }
}

