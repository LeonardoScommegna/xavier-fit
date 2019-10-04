package com.fteotini.amf.its.launcher.DONTRUN;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("DONT_RUN")
public class DummyTest {
    @Test
    void it_is_green() {
        assertThat(true).isTrue();
    }
}
