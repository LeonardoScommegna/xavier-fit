package com.fteotini.amf.launcher.process.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

class MinionInputStreamHandler {
    private final InputStream is;

    MinionInputStreamHandler(final InputStream is) {
        this.is = is;
    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    <T extends Serializable> T readObject(Class<T> type) throws IOException, ClassNotFoundException {
        try (var objReader = new ObjectInputStream(is)) {
            return (T) objReader.readObject();
        }
    }
}
