package com.fteotini.Xavier.launcher;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class MinionOutputStreamHandlerTest {

    @Test
    void Given_an_object_it_should_be_able_to_write_it_to_an_OutputStream() throws IOException {
        var outputStream = new ByteArrayOutputStream();

        var obj = new DummyDTO("foo");
        new MinionOutputStreamHandler(outputStream).writeObject(obj);

        var actualObj = readSerializedObj(outputStream);
        assertThat(actualObj).isExactlyInstanceOf(DummyDTO.class);
        assertThat(((DummyDTO) actualObj).prop).isEqualTo("foo");
    }

    private Object readSerializedObj(ByteArrayOutputStream outputStream) {
        try (var reader = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()))) {
            return reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class DummyDTO implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final String prop;

        DummyDTO(String prop) {
            this.prop = prop;
        }
    }
}