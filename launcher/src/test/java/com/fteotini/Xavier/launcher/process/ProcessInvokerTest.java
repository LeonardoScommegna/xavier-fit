package com.fteotini.Xavier.launcher.process;

import com.fteotini.Xavier.launcher.minion.MinionEntryPoint;
import com.google.common.util.concurrent.Futures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.StartedProcess;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ProcessInvokerTest {
    @Mock(answer = Answers.RETURNS_SELF)
    private ProcessExecutor processExecutor;
    @Mock
    private ExecuteStreamHandler commsHandler;
    @Mock
    private ProcessArgs processArgs;
    @Mock
    private StartedProcess startedProcess;
    private ProcessInvoker sut;

    @BeforeEach
    void setUp() throws IOException {
        when(processExecutor.start()).thenReturn(startedProcess);
        sut = new ProcessInvoker(processArgs,commsHandler,() ->processExecutor);
    }

    @Test
    void When_startMinionProcess_then_it_should_set_the_forked_jvm_to_be_destroyed_on_vm_exit() throws IOException {
        sut.startMinionProcess();

        verify(processExecutor).destroyOnExit();
    }

    @Test
    void Given_a_processArgs_obj_then_it_should_build_the_command_correctly() throws IOException {
        var command = List.of("foo","bar");
        when(processArgs.buildArgsList()).thenReturn(command);

        sut.startMinionProcess();

        var expectedCommand = new ArrayList<>(command);
        expectedCommand.add(MinionEntryPoint.class.getCanonicalName());
        verify(processExecutor).command(expectedCommand);
    }

    @Test
    void When_startMinionProcess_then_it_should_pass_the_comms_handler_to_the_process_builder() throws IOException {
        sut.startMinionProcess();
        verify(processExecutor).streams(commsHandler);
    }

    @Test
    void When_startMinionProcess_then_its_output_should_be_the_same_as_the_process() throws IOException {
        var expected = Futures.immediateFuture(new ProcessResult(2, null));
        when(startedProcess.getFuture()).thenReturn(expected);

        var result = sut.startMinionProcess();

        assertThat(result).isEqualTo(expected);
    }
}