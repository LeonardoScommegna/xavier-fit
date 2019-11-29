package com.fteotini.Xavier.its.launcher.DONTRUN;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("DONT_RUN")
class Dummy2Test {

    @Test
    void It_Fails() {
        throw new RuntimeException("must fail");
    }

    @Test
    void it_passes() {
        assertThat(true).isTrue();
    }
}
