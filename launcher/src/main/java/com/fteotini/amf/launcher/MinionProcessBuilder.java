package com.fteotini.amf.launcher;

import com.fteotini.amf.launcher.minion.MinionArgs;
import com.fteotini.amf.launcher.process.ProcessInvoker;
import com.fteotini.amf.launcher.util.ClassPathResolver;
import com.fteotini.amf.launcher.util.JavaExecutableLocator;

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
