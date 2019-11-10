package com.fteotini.amf.commons.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathResolverTest {
    @Test
    void It_should_return_the_currently_loaded_classPath() {
        var currentlyLoadedCP = Arrays.asList(System.getProperty("java.class.path").split(File.pathSeparator));

        var result = new ClassPathResolver().getClassPaths();

        assertThat(result).extracting(Path::toString).containsAll(currentlyLoadedCP);
    }

    @Test
    void Given_a_non_existing_path_in_the_currently_loaded_classPath_then_it_should_filter_it_out() {
        var key = "java.class.path";
        var oldCP = System.getProperty(key);

        try {
            System.setProperty(key, oldCP + File.pathSeparator + "not_existing");

            var result = new ClassPathResolver().getClassPaths();

            assertThat(result).noneSatisfy(p -> assertThat(p.toString()).endsWith("not_existing"));
        } finally {
            System.setProperty(key, oldCP);
        }
    }
}