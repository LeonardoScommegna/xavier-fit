package com.fteotini.amf.launcher.util;

import java.io.*;

class OutputStreamReader {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    OutputStream getOutputStream() {
        return outputStream;
    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    <T extends Serializable> T readObject(Class<T> type) throws IOException, ClassNotFoundException {
        var byteStream = new ByteArrayInputStream(outputStream.toByteArray());
        try (var reader = new ObjectInputStream(byteStream)) {
            return (T) reader.readObject();
        }
    }
}
