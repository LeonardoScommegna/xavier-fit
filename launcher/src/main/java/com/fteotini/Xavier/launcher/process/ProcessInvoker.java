package com.fteotini.Xavier.launcher.process;

import com.fteotini.Xavier.launcher.minion.MinionEntryPoint;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ProcessInvoker {
    private final ProcessArgs processArgs;
    private final ExecuteStreamHandler processCommunicationHandler;
    private final Supplier<ProcessExecutor> processExecutorSupplier;

    public ProcessInvoker(ProcessArgs processArgs, ExecuteStreamHandler processCommunicationHandler) {
        this(processArgs, processCommunicationHandler, ProcessExecutor::new);
    }

    ProcessInvoker(ProcessArgs processArgs, ExecuteStreamHandler processCommunicationHandler, Supplier<ProcessExecutor> processExecutorSupplier) {
        this.processArgs = processArgs;
        this.processCommunicationHandler = processCommunicationHandler;
        this.processExecutorSupplier = processExecutorSupplier;
    }

    public Future<ProcessResult> startMinionProcess() throws IOException {
        var command = getArgsList();
        return processExecutorSupplier.get()
                .command(command)
                //TODO: maybe not? or maybe don't redirect the error stream to the global system err
                .redirectErrorStream(false)
                .streams(processCommunicationHandler)
                .destroyOnExit()
                .start()
                .getFuture();
    }

    private List<String> getArgsList() {
        var args = new ArrayList<>(processArgs.buildArgsList());
        args.add(MinionEntryPoint.class.getCanonicalName());
        return args;
    }
}

