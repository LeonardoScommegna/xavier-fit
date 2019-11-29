package com.fteotini.Xavier.tester.providers.JUnit5;

import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestExecutionSummary;
import com.fteotini.Xavier.tester.providers.JUnit5.ExecutionSummaryGenerator.TestExecutionSummaryGeneratingListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class JUnit5TestRunnerTest {
    @Mock
    private DiscoveryRequestBuilder requestBuilder;
    @Mock(lenient = true)
    private ContextualTestRunner ctxRunner;
    @Mock
    private Launcher junitLauncher;
    @Mock
    private TestExecutionSummaryGeneratingListener listener;

    private JUnit5TestRunner sut;

    @BeforeEach
    void setUp() {
        sut = spy(new JUnit5TestRunner(requestBuilder, ctxRunner, () -> junitLauncher));
        when(sut.makeListener()).thenReturn(listener);
        when(ctxRunner.run(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Supplier.class).get());
    }

    @Test
    void When_calling_run_it_should_register_as_listener_TestExecutionSummaryGeneratingListener() {
        sut.run();

        var listenerCaptor = ArgumentCaptor.forClass(TestExecutionListener.class);
        verify(junitLauncher).registerTestExecutionListeners(listenerCaptor.capture());

        assertThat(listenerCaptor.getValue()).isInstanceOf(TestExecutionSummaryGeneratingListener.class);
    }

    @Test
    void When_calling_run_it_should_return_the_return_value_from_the_listener() {
        var expectedResult = new TestExecutionSummary(Collections.emptySet());
        when(listener.generateTestSuiteOutcome()).thenReturn(expectedResult);

        var result = sut.run();

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void When_calling_run_it_should_call_the_requestBuilder_and_pass_its_result_to_the_launcher() {
        var expected = mock(LauncherDiscoveryRequest.class);
        when(requestBuilder.build()).thenReturn(expected);

        sut.run();

        verify(junitLauncher).execute(expected);
    }

    @Test
    void When_calling_run_it_should_wrap_the_execution_in_a_function_and_pass_it_to_the_ctxRunner() {
        var expected = mock(LauncherDiscoveryRequest.class);
        when(requestBuilder.build()).thenReturn(expected);

        doAnswer(invocationOnMock -> {
            var res = invocationOnMock.getArgument(0, Supplier.class).get();
            verify(junitLauncher).execute(expected);
            return res;
        }).when(ctxRunner).run(any());

        sut.run();
    }
}