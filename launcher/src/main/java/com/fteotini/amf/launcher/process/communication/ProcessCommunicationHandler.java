package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.launcher.util.MinionInputStreamHandler;
import com.fteotini.amf.launcher.util.MinionOutputStreamHandler;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProcessCommunicationHandler implements ExecuteStreamHandler {

    private final Function<OutputStream, MinionOutputStreamHandler> outputStreamHandlerFactory;
    private final Function<InputStream, MinionInputStreamHandler> inputStreamHandlerFactory;
    private final Consumer<MinionOutputStreamHandler> sendInitialData;
    private final Consumer<MinionInputStreamHandler> receiveData;

    public ProcessCommunicationHandler(Function<OutputStream, MinionOutputStreamHandler> outputStreamHandlerFactory,
                                       Function<InputStream, MinionInputStreamHandler> inputStreamHandlerFactory,
                                       Consumer<MinionOutputStreamHandler> sendInitialData,
                                       Consumer<MinionInputStreamHandler> receiveData) {

        this.outputStreamHandlerFactory = outputStreamHandlerFactory;
        this.inputStreamHandlerFactory = inputStreamHandlerFactory;
        this.sendInitialData = sendInitialData;
        this.receiveData = receiveData;
    }

    @Override
    public void setProcessInputStream(OutputStream os) throws IOException {
        sendInitialData.accept(outputStreamHandlerFactory.apply(os));
    }

    @Override
    public void setProcessErrorStream(InputStream is) throws IOException {
    }

    @Override
    public void setProcessOutputStream(InputStream is) throws IOException {
        receiveData.accept(inputStreamHandlerFactory.apply(is));
    }

    @Override
    public void start() throws IOException {
    }

    @Override
    public void stop() {

    }
}
