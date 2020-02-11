package com.fteotini.Xavier.launcher;

import com.fteotini.Xavier.commons.util.ClassPathResolver;
import com.fteotini.Xavier.commons.util.JavaExecutableLocator;
import com.fteotini.Xavier.launcher.minion.MinionArgs;
import com.fteotini.Xavier.launcher.minion.MinionResult;
import com.fteotini.Xavier.launcher.process.ProcessArgs;
import com.fteotini.Xavier.launcher.process.ProcessInvoker;
import com.fteotini.Xavier.launcher.process.communication.ProcessCommunicationHandler;
import com.fteotini.Xavier.launcher.process.communication.ReceiveData;
import com.fteotini.Xavier.launcher.process.communication.SendInitialData;
import com.spikhalskiy.futurity.Futurity;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MinionProcessBuilder {
    private final MinionArgs minionArgs;

    private Set<Path> classPath = makeClassPathResolver().getClassPaths();
    private Path javaExecutableLocation = makeJavaExecutableLocator().getLocation();
    private boolean withDebugger = false;
    private int debugPort = 5005;

    public MinionProcessBuilder(final MinionArgs minionArgs) {
        this.minionArgs = minionArgs;
    }

    public MinionProcessBuilder withDebugger() {
        withDebugger = true;
        return this;
    }

    public MinionProcessBuilder withDebugger(int debugPort) {
        this.debugPort = debugPort;
        return withDebugger();
    }

    public CompletableFuture<MinionResult> start() throws IOException {
        var receiveData = new ReceiveData();
        var invoker = new ProcessInvoker(buildProcessArgs(), buildCommunicationHandler(new SendInitialData(minionArgs), receiveData));

        return Futurity.shift(invoker.startMinionProcess())
                .thenApply(processResult -> new MinionResult(processResult, Arrays.asList(receiveData.receive())));
    }

    private ExecuteStreamHandler buildCommunicationHandler(SendInitialData sendInitialData, ReceiveData receiveData) {
        return new ProcessCommunicationHandler(MinionOutputStreamHandler::new, MinionInputStreamHandler::new, sendInitialData, receiveData);
    }

    private ProcessArgs buildProcessArgs() {
        if (withDebugger) {
            return new ProcessArgs(javaExecutableLocation, classPath, debugPort);
        }
        return new ProcessArgs(javaExecutableLocation, classPath);
    }

    ClassPathResolver makeClassPathResolver() {
        return new ClassPathResolver();
    }

    @SuppressWarnings("WeakerAccess")
    JavaExecutableLocator makeJavaExecutableLocator() {
        return new JavaExecutableLocator();
    }

    public MinionProcessBuilder withClassPath(Set<Path> classPath) {
        this.classPath = classPath;
        return this;
    }

    public MinionProcessBuilder withJavaExecutableLocation(Path javaExecutableLocation) {
        this.javaExecutableLocation = javaExecutableLocation;
        return this;
    }
}
