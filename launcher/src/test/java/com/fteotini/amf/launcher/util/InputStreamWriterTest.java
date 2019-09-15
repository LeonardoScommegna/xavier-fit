package com.fteotini.amf.launcher.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class InputStreamWriterTest {

    private InputStreamWriter sut;

    @BeforeEach
    void setUp() {
        sut = new InputStreamWriter();
    }

    @Test
    void Given_an_object_it_should_be_able_to_write_it_to_an_InputStream() throws IOException {
        var obj = new DummyDTO("foo");

        var result = sut.writeObject(obj).create();

        var actualObj = readSerializedObj(result);
        assertThat(actualObj).isExactlyInstanceOf(DummyDTO.class);
        assertThat(((DummyDTO) actualObj).prop).isEqualTo("foo");
    }

    private static RuntimeException toUnchecked(Throwable e) {
        return new RuntimeException(e);
    }

    private Object readSerializedObj(InputStream inputStream) {
        try (var reader = new ObjectInputStream(inputStream)) {
            return reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw toUnchecked(e);
        }
    }

    static class DummyDTO implements Serializable {
        final String prop;

        DummyDTO(String prop) {
            this.prop = prop;
        }
    }
}