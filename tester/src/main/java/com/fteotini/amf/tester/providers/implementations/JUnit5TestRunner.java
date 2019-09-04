package com.fteotini.amf.tester.providers.implementations;

import com.fteotini.amf.tester.TestMethodOutcome;
import com.fteotini.amf.tester.TestRunner;
import com.fteotini.amf.tester.TestSuiteOutcome;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

class JUnit5TestRunner implements TestRunner {
    private final Set<Path> additionalClassPaths;
    private final Launcher launcher;

    JUnit5TestRunner(Set<Path> additionalClassPaths, Supplier<Launcher> launcherFactory) {
        this.additionalClassPaths = additionalClassPaths;
        launcher = launcherFactory.get();
    }

    public JUnit5TestRunner(Set<Path> additionalClassPaths) {
        this(additionalClassPaths, LauncherFactory::create);
    }

    @Override
    public TestSuiteOutcome runEntireSuite() {
        var originalClassLoader = getDefaultClassLoader();
        var url = additionalClassPaths.stream().map(JUnit5TestRunner::ToURL).toArray(URL[]::new);
        var newClassLoader = URLClassLoader.newInstance(url, originalClassLoader);

//        var f = new Directory(classPathResource);
        try {
            Thread.currentThread().setContextClassLoader(newClassLoader);

            var discover = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClasspathRoots(additionalClassPaths))
                    .filters(includeClassNamePatterns(".*Test"))
                    .build();

            var res = new SummaryGeneratingListener();
            launcher.registerTestExecutionListeners(res);

            var plan = launcher.discover(discover);

            launcher.execute(plan);
            return null;
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    @Override
    public <T> TestMethodOutcome runSingleMethod(Class<T> clazz, Function<T, Method> methodSelector) {
        return null;
    }

    private static URL ToURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            /* ignore */
        }
        return ClassLoader.getSystemClassLoader();
    }
}
