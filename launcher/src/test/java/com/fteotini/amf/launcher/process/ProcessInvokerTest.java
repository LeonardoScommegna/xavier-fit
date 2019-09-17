package com.fteotini.amf.launcher.process;

import com.fteotini.amf.launcher.minion.MinionArgs;
import com.fteotini.amf.launcher.process.ProcessArgs;
import com.fteotini.amf.launcher.process.ProcessInvoker;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.zeroturnaround.exec.ProcessExecutor;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ProcessInvokerTest {
    @Mock(answer = Answers.RETURNS_SELF)
    private ProcessExecutor processExecutor;

    private ProcessInvoker buildSut(ProcessArgs processArgs, MinionArgs minionArgs) {
        return new ProcessInvoker(processArgs,minionArgs,() -> processExecutor);
    }
}