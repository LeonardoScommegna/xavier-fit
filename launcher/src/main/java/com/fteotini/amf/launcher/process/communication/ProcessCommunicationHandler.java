package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.launcher.MinionInputStreamHandler;
import com.fteotini.amf.launcher.MinionOutputStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProcessCommunicationHandler implements ExecuteStreamHandler {
    private static final Logger log = LoggerFactory.getLogger(ProcessCommunicationHandler.class);


    private final Function<OutputStream, MinionOutputStreamHandler> outputStreamHandlerFactory;
    private final Function<InputStream, MinionInputStreamHandler> inputStreamHandlerFactory;
    private final Consumer<MinionOutputStreamHandler> sendInitialData;
    private final Consumer<MinionInputStreamHandler> receiveData;

    private final Queue<Thread> streamThreads = new ArrayDeque<>(3);

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
        if (sendInitialData != null) {
            var threadName = "processInputStream";
            log.trace("Creating the {} thread.", threadName);

            final Runnable runnable = () -> sendInitialData.accept(outputStreamHandlerFactory.apply(os));
            streamThreads.add(makeThread(runnable, threadName));
        }
    }

    @Override
    public void setProcessErrorStream(InputStream is) {
        var threadName = "processErrorStream";
        log.trace("Creating the {} thread.", threadName);

        final Runnable runnable = () -> handleErr(is);
        streamThreads.add(makeThread(runnable, threadName));
    }

    @Override
    public void setProcessOutputStream(InputStream is) {
        if (receiveData != null) {
            var threadName = "processOutputStream";
            log.trace("Creating the {} thread.", threadName);

            final Runnable runnable = () -> receiveData.accept(inputStreamHandlerFactory.apply(is));
            streamThreads.add(makeThread(runnable, threadName));
        }
    }

    @Override
    public void start() throws IOException {
        log.debug("Starting the comms threads...");
        streamThreads.forEach(Thread::start);
    }

    @Override
    public void stop() {
        log.debug("Stopping the comms threads...");
        while (!streamThreads.isEmpty()) {
            var thread = streamThreads.poll();
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static Thread makeThread(Runnable runnable, String threadName) {
        var thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName(threadName);
        return thread;
    }

    private static void handleErr(InputStream processErrorStream) {
        int length;
        var buf = new byte[1024];
        try {
            while ((length = processErrorStream.read(buf)) > 0) {
                System.err.write(buf, 0, length);
                System.err.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
