package com.fteotini.amf.launcher.process.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

class MinionOutputStreamHandler {
    private final OutputStream os;

    MinionOutputStreamHandler(final OutputStream os) {
        this.os = os;
    }

    <T extends Serializable> void writeObject(final T value) throws IOException {
        try (var writer = new ObjectOutputStream(os)) {
            writer.writeObject(value);
            writer.flush();
        }
    }
}
