package com.fteotini.amf.launcher.process;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessArgsTest {
    private static final String MODULE_NAME = "launcher";

    @Test
    void Given_a_javaExecPath_and_an_empty_set_of_classPaths_then_the_argsArray_should_have_size_1() {
        var result = new ProcessArgs(Path.of("exec"), Collections.emptySet()).buildArgsList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).endsWith(stringPath("exec"));
    }

    @Test
    void Given_a_javaExecPath_and_a_list_of_classPaths_then_the_argsArray_should_have_size_3() {
        var result = new ProcessArgs(Path.of("exec"), Set.of(Path.of("foo"), Path.of("bar"))).buildArgsList();

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).endsWith(stringPath("exec"));
        assertThat(result.get(1)).isEqualTo("-cl");

        assertThat(result.get(2).split(File.pathSeparator))
                .hasSize(2)
                .anySatisfy(s -> assertThat(s).endsWith(stringPath("foo")))
                .anySatisfy(s -> assertThat(s).endsWith(stringPath("bar")));

    }

    @Test
    void Given_a_debugPort_then_it_should_build_the_correct_arg() {
        var result = new ProcessArgs(Path.of("exec"), Collections.emptySet(), 5555).buildArgsList();

        assertThat(result).hasSize(2);
        assertThat(result.get(1)).startsWith("-agentlib:jdwp").containsPattern("address=.+?:5555");
    }

    private static String stringPath(String lastFragment) {
        return MODULE_NAME + File.separator + lastFragment;
    }
}