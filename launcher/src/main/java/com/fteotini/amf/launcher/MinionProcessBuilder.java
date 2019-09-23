package com.fteotini.amf.launcher;

import com.fteotini.amf.commons.util.ClassPathResolver;
import com.fteotini.amf.commons.util.JavaExecutableLocator;
import com.fteotini.amf.launcher.minion.MinionArgs;
import com.fteotini.amf.launcher.process.ProcessArgs;
import com.fteotini.amf.launcher.process.ProcessInvoker;
import com.fteotini.amf.launcher.process.communication.ProcessCommunicationHandler;
import com.fteotini.amf.launcher.process.communication.ReceiveData;
import com.fteotini.amf.launcher.process.communication.SendInitialData;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.nio.file.Path;
import java.util.Set;

public class MinionProcessBuilder {
    private final MinionArgs minionArgs;

    private Set<Path> classPath = makeClassPathResolver().getClassPaths();
    private Path javaExecutableLocation = makeJavaExecutableLocator().getLocation();
    private boolean withDebugger = false;
    private Integer debugPort;

    public MinionProcessBuilder(final MinionArgs minionArgs) {
        this.minionArgs = minionArgs;
    }

    public MinionProcessBuilder withDebugger() {
        return withDebugger(null);
    }

    public MinionProcessBuilder withDebugger(Integer debugPort) {
        withDebugger = true;
        this.debugPort = debugPort;

        return this;
    }

    public ProcessInvoker build() {
        return new ProcessInvoker(buildProcessArgs(), buildCommunicationHandler());
    }

    private ExecuteStreamHandler buildCommunicationHandler() {
        return new ProcessCommunicationHandler(MinionOutputStreamHandler::new, MinionInputStreamHandler::new, new SendInitialData(minionArgs), new ReceiveData());
    }

    private ProcessArgs buildProcessArgs() {
        if (withDebugger) {
            return new ProcessArgs(javaExecutableLocation, classPath, debugPort);
        }
        return new ProcessArgs(javaExecutableLocation, classPath);
    }

    @SuppressWarnings("WeakerAccess")
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
