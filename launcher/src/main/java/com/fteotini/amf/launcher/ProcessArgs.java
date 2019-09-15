package com.fteotini.amf.launcher;

import com.google.common.base.Preconditions;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessArgs {
    private final Set<Path> launchClasspath;
    private final Path javaExecutable;

    private final boolean withDebugger;
    private final Integer debugPort;

    public ProcessArgs(Path javaExecutable, Set<Path> launchClasspath) {
        this(javaExecutable, launchClasspath, false, null);
    }

    public ProcessArgs(Path javaExecutable, Set<Path> launchClasspath, int debugPort) {
        this(javaExecutable, launchClasspath, true, debugPort);
    }

    private ProcessArgs(Path javaExecutable, Set<Path> launchClasspath, boolean withDebugger, Integer debugPort) {
        this.launchClasspath = Preconditions.checkNotNull(launchClasspath);
        this.javaExecutable = Preconditions.checkNotNull(javaExecutable);

        this.withDebugger = withDebugger;

        Preconditions.checkArgument(debugPort == null || (debugPort >= 1024 && debugPort <= 65535), "'debugPort' value [%d] is not in the valid range", debugPort);
        this.debugPort = debugPort;
    }

    public String[] buildArgsArray() {
        var args = new LinkedList<String>();
        args.add(getAbsolutePathAsString(javaExecutable));
        args.addAll(classPathArgs());

        if (withDebugger) {
            args.add(debugArgument());
        }

        return args.toArray(String[]::new);
    }

    private static String getAbsolutePathAsString(Path path) {
        return path.toAbsolutePath().toString();
    }

    private String debugArgument() {
        return "-agentlib:jdwp=transport=dt_socket,server=n,address=0.0.0.0:" + debugPort + ",suspend=y";
    }

    private List<String> classPathArgs() {
        if (launchClasspath.isEmpty())
            return Collections.emptyList();

        var classPathString = launchClasspath.stream()
                .map(ProcessArgs::getAbsolutePathAsString)
                .collect(Collectors.joining(File.pathSeparator));

        return List.of("-cl", classPathString);
    }
}
