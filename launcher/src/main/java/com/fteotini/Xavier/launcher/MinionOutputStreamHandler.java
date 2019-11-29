package com.fteotini.Xavier.launcher;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class MinionOutputStreamHandler {
    private final OutputStream os;

    public MinionOutputStreamHandler(final OutputStream os) {
        this.os = os;
    }

    public <T extends Serializable> void writeObject(final T value) throws IOException {
        try (var writer = new ObjectOutputStream(os)) {
            writer.writeObject(value);
            writer.flush();
        }
    }
}
