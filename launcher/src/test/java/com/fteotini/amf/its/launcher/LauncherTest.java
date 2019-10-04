package com.fteotini.amf.its.launcher;

import com.fteotini.amf.commons.tester.ExecutionSummary.ExecutionResult;
import com.fteotini.amf.commons.tester.ExecutionSummary.TestEntityType;
import com.fteotini.amf.commons.tester.MethodUnderTest;
import com.fteotini.amf.its.launcher.DONTRUN.DummyTest;
import com.fteotini.amf.launcher.MinionProcessBuilder;
import com.fteotini.amf.launcher.minion.MinionArgs;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class LauncherTest {
    @Test
    void it_can_launch_the_tests_in_a_forked_jvm() throws NoSuchMethodException, IOException, ExecutionException, InterruptedException {
        var args = MinionArgs.ForSingleMethod(new MethodUnderTest(DummyTest.class, DummyTest.class.getDeclaredMethod("it_is_green")));

        var minionResult = new MinionProcessBuilder(args)
                //.withDebugger(50005)
                .start()
                .get();

        assertThat(minionResult.getProcessResult().getExitValue()).isEqualTo(0);

        var classesUnderTest = minionResult.getTestExecutionSummary().getTestContainers();
        assertThat(classesUnderTest)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(c -> assertThat(c.getEntityName()).isEqualTo(DummyTest.class.getSimpleName()));

        var methodUnderTests = classesUnderTest.iterator().next().getChildren();
        assertThat(methodUnderTests)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(t -> {
                    assertThat(t.getEntityName()).isEqualTo("it_is_green()");
                    assertThat(t.getResult()).isEqualTo(ExecutionResult.Success);
                    assertThat(t.getType()).isEqualTo(TestEntityType.Method);
                });
    }
}
