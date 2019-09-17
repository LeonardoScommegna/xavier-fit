package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.launcher.util.MinionOutputStreamHandler;

import java.util.function.Consumer;

public class SendInitialData implements Consumer<MinionOutputStreamHandler> {
    @Override
    public void accept(MinionOutputStreamHandler minionOutputStreamHandler) {
        throw new RuntimeException("Not Implemented");
    }
}
