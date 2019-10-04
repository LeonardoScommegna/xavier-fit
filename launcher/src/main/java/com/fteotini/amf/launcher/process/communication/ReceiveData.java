package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.commons.tester.ExecutionSummary.TestExecutionSummary;
import com.fteotini.amf.launcher.MinionInputStreamHandler;

import java.io.IOException;
import java.util.function.Consumer;

public class ReceiveData implements Consumer<MinionInputStreamHandler> {
    private TestExecutionSummary result;

    @Override
    public void accept(MinionInputStreamHandler inputStreamHandler) {
        try {
            result = inputStreamHandler.readObject(TestExecutionSummary.class);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TestExecutionSummary receive() {
        return result;
    }
}
