package com.fteotini.amf.launcher.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class OutputStreamReaderTest {
    private final OutputStreamReader sut = new OutputStreamReader();

    @Test
    void Given_an_OutputStream_containing_a_serialized_obj_it_should_be_able_to_read_it() throws IOException, ClassNotFoundException {
        var expected = new DummyDTO("bar");
        writeObject(sut.getOutputStream(), expected);

        var actual = sut.readObject(DummyDTO.class);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private static void writeObject(OutputStream outputStream, Object value) {
        try (var writer = new ObjectOutputStream(outputStream)) {
            writer.writeObject(value);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class DummyDTO implements Serializable {
        final String prop;

        DummyDTO(String prop) {
            this.prop = prop;
        }
    }
}