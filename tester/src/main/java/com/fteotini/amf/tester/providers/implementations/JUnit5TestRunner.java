package com.fteotini.amf.tester.providers.implementations;

import com.fteotini.amf.tester.SuiteOutcome;
import com.fteotini.amf.tester.TestRunner;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import static org.junit.platform.engine.discovery.ClassNameFilter.*;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.platform.engine.discovery.DiscoverySelectors.*;

public class JUnit5TestRunner implements TestRunner {
    private final Launcher launcher;

    public JUnit5TestRunner(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public SuiteOutcome runEntireSuite(String classPathResource) {
        var originalClassLoader = Thread.currentThread().getContextClassLoader();
        var ccc = originalClassLoader.getResource(classPathResource);
        var newClassLoader = URLClassLoader.newInstance(new URL[]{ccc},originalClassLoader);

//        var f = new Directory(classPathResource);
        try {
            //Thread.currentThread().setContextClassLoader(newClassLoader);

            var discover = LauncherDiscoveryRequestBuilder.request()
        .selectors(
//                selectClasspathRoots(Collections.singleton(convertClassPathToUri(ccc)))
                //selectClasspathResource(ccc.toString())
    //            selectPackage("com.fteotini.amf.tester.dummyProject")
                selectUri(ccc.toString())
        )
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

    private static Path convertClassPathToUri(URL classPathResource) {
        try {
            return Path.of(classPathResource.toURI());
        } catch (URISyntaxException e) {
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
