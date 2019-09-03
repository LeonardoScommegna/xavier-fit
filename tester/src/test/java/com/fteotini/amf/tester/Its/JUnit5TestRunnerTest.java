package com.fteotini.amf.tester.Its;

import com.fteotini.amf.tester.providers.implementations.JUnit5TestRunner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnit5TestRunnerTest {

    @Test
    @Disabled
    void it_can_run_a_suite() throws URISyntaxException {
        var launcher = LauncherFactory.create();

        var result = new JUnit5TestRunner(launcher).runEntireSuite("com/fteotini/amf/tester/dummyProject");

        assertThat(result).isNotNull();
    }
}
