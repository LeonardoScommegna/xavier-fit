package com.fteotini.amf.tester.providers.implementations;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Tag("IntegrationTest")
class JUnit5TestRunnerTest {

    private static final String SUREFIRE_VERSION = System.getProperty("surefire.version");
    private static final String COMPILER_VERSION = System.getProperty("compiler.version");

    @Test
    void it_can_run_a_suite() throws MavenInvocationException, URISyntaxException {
        var projectName = "JUnit5TestRunner_Integration_project";
        BuildTestSubProject(projectName);

        var cp = Path.of(getClass().getClassLoader().getResource(projectName + "/target/test-classes").toURI());

        new JUnit5TestRunner(Set.of(cp)).runEntireSuite();
    }

    private void BuildTestSubProject(String projectName) throws MavenInvocationException {
        var pomUrl = getClass().getClassLoader().getResource(projectName + "/pom.xml");

        var invocationRequest = new DefaultInvocationRequest();
        invocationRequest.setPomFile(new File(pomUrl.getFile()));
        invocationRequest.setBatchMode(true);
        invocationRequest.setGoals(BuildGoalsList("clean", "test-compile"));

        var invoker = new DefaultInvoker();
        var result = invoker.execute(invocationRequest);

        if (result.getExitCode() != 0) {
            throw new RuntimeException("maven error");
        }
    }

    private static List<String> BuildGoalsList(String... goals) {
        var list = new ArrayList<String>();

        list.add("-Dsurefire.version=" + SUREFIRE_VERSION);
        list.add("-Dcompiler.version=" + COMPILER_VERSION);
        list.add("-q");

        list.addAll(Arrays.asList(goals));

        return list;
    }
}
