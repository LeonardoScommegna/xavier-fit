package com.fteotini.Xavier.tester.providers.JUnit5;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Supplier;

class ContextualTestRunner {
    private final Set<Path> additionalClassPaths;
    private final Supplier<Thread> currentThreadProvider;

    ContextualTestRunner(Set<Path> additionalClassPaths) {
        this(additionalClassPaths, Thread::currentThread);
    }

    ContextualTestRunner(Set<Path> additionalClassPaths, Supplier<Thread> currentThreadProvider) {
        this.additionalClassPaths = additionalClassPaths;
        this.currentThreadProvider = currentThreadProvider;
    }

    <T> T run(Supplier<T> runnable) {
        if (additionalClassPaths.isEmpty())
            return runnable.get();

        var currentThread = currentThreadProvider.get();

        var original = currentThread.getContextClassLoader();
        var newClassLoader = buildNewClassLoader(original);

        try {
            currentThread.setContextClassLoader(newClassLoader);
            return runnable.get();
        } finally {
            currentThread.setContextClassLoader(original);
        }
    }

    private ClassLoader buildNewClassLoader(ClassLoader original) {
        var classPathURLs = additionalClassPaths.stream().map(ContextualTestRunner::toURL).toArray(URL[]::new);
        return URLClassLoader.newInstance(classPathURLs,original);
    }

    private static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
