package com.fteotini.Xavier.launcher.process.communication;

import com.fteotini.Xavier.launcher.MinionOutputStreamHandler;
import com.fteotini.Xavier.launcher.minion.MinionArgs;

import java.io.IOException;
import java.util.function.Consumer;

public class SendInitialData implements Consumer<MinionOutputStreamHandler> {

    private final MinionArgs initialData;

    public SendInitialData(final MinionArgs initialData) {
        this.initialData = initialData;
    }

    @Override
    public void accept(MinionOutputStreamHandler minionOutputStreamHandler) {
        try {
            minionOutputStreamHandler.writeObject(initialData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
