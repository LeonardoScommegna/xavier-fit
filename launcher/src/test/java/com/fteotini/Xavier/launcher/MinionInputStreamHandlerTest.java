package com.fteotini.Xavier.launcher;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class MinionInputStreamHandlerTest {

    private static InputStream writeObjectToInputStream(Object value) {
        var os = new ByteArrayOutputStream();
        try (var writer = new ObjectOutputStream(os)) {
            writer.writeObject(value);
            writer.flush();

            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Given_an_InputStream_containing_a_serialized_obj_it_should_be_able_to_read_it() throws IOException, ClassNotFoundException {
        var expected = new DummyDTO("bar");
        var sut = new MinionInputStreamHandler(writeObjectToInputStream(expected));

        var result = sut.readObject(DummyDTO.class);
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    static class DummyDTO implements Serializable {
        final String prop;

        DummyDTO(String prop) {
            this.prop = prop;
        }
    }
}