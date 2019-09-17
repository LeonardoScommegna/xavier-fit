package com.fteotini.amf.launcher.process;

import com.fteotini.amf.launcher.minion.MinionEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.util.ArrayList;
import java.util.List;

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
    private ProcessInvoker sut;

    @BeforeEach
    void setUp() {
        sut = new ProcessInvoker(processArgs,commsHandler,() ->processExecutor);
    }

    @Test
    void Given_a_processArgs_obj_then_it_should_build_the_command_correctly() {
        var command = List.of("foo","bar");
        when(processArgs.buildArgsList()).thenReturn(command);

        sut.startMinionProcess();

        var expectedCommand = new ArrayList<>(command);
        expectedCommand.add(MinionEntryPoint.class.getCanonicalName());
        verify(processExecutor).command(expectedCommand);
    }
}