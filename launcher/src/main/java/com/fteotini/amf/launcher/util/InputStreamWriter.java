package com.fteotini.amf.launcher.util;

import java.io.*;

class InputStreamWriter {
    private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

    <T extends Serializable> InputStreamWriter writeObject(final T value) throws IOException {
        try (var writer = new ObjectOutputStream(byteStream)) {
            writer.writeObject(value);
            writer.flush();
        }
        return this;
    }

    InputStream create() {
        return new ByteArrayInputStream(byteStream.toByteArray());
    }
}
