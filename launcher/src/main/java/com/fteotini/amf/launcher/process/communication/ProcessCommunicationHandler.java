package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.launcher.MinionInputStreamHandler;
import com.fteotini.amf.launcher.MinionOutputStreamHandler;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

//TODO: maybe it's better to handle each stream in a separate thread
public class ProcessCommunicationHandler implements ExecuteStreamHandler {

    private final Function<OutputStream, MinionOutputStreamHandler> outputStreamHandlerFactory;
    private final Function<InputStream, MinionInputStreamHandler> inputStreamHandlerFactory;
    private final Consumer<MinionOutputStreamHandler> sendInitialData;
    private final Consumer<MinionInputStreamHandler> receiveData;
    private OutputStream processOutputStream;
    private InputStream processInputStream;
    private InputStream processErrorStream;

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
    public void setProcessInputStream(OutputStream os) {
        processOutputStream = os;
    }

    @Override
    public void setProcessErrorStream(InputStream is) {
        processErrorStream = is;
    }

    @Override
    public void setProcessOutputStream(InputStream is) {
        processInputStream = is;
    }

    @Override
    public void start() throws IOException {
        if (processOutputStream != null)
            sendInitialData.accept(outputStreamHandlerFactory.apply(this.processOutputStream));
        if (processErrorStream != null)
            handleErr(processErrorStream);
        if (processInputStream != null)
            receiveData.accept(inputStreamHandlerFactory.apply(this.processInputStream));
    }

    @Override
    public void stop() {

    }

    private static void handleErr(InputStream processErrorStream) throws IOException {
        int length;
        var buf = new byte[1024];
        while ((length = processErrorStream.read(buf)) > 0) {
            System.err.write(buf, 0, length);
            System.err.flush();
        }
    }
}
