package com.fteotini.Xavier.tester.integrationTests.providers.JUnit5;

import com.fteotini.Xavier.commons.tester.ExecutionSummary.ExecutionResult;
import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestEntity;
import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestEntityType;
import com.fteotini.Xavier.commons.tester.TestExecutionMode;
import com.fteotini.Xavier.tester.TestDiscoveryOptions;
import com.fteotini.Xavier.tester.TestRunner;
import com.fteotini.Xavier.tester.providers.JUnit5.JUnit5TestRunnerFactory;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class JUnit5TestRunnerTest {
    private static final String SUREFIRE_VERSION = System.getProperty("surefire.version");
    private static final String COMPILER_VERSION = System.getProperty("compiler.version");

    private static final String PROJECT_NAME = "JUnit5TestRunner_Integration_project";
    private static final Path PROJECT_PATH = Path.of("src", "test", "resources", PROJECT_NAME);

    private static final JUnit5TestRunnerFactory testRunnerFactory = new JUnit5TestRunnerFactory();

    @BeforeAll
    static void init() throws MavenInvocationException {
        BuildTestSubProject();
    }

    @AfterAll
    static void dispose() throws IOException {
        Files.walk(PROJECT_PATH.resolve("target"))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void it_can_run_a_suite() {
        var sut = buildTestRunnerForSubProject();
        var result = sut.run().getTestContainers();

        assertThat(result).hasSize(4);
        assertThat(result.stream().map(TestEntity::getEntityName)).containsExactlyInAnyOrder(
                "SkippedClassTest",
                "With2SuccessfulTest",
                "With1FailingAnd1SuccessfulTest",
                "With2SkippedAnd1SuccessfulTest"
        );
        assertThat(result).allSatisfy(e -> assertThat(e.getType()).isEqualTo(TestEntityType.Class));

        var skippedClassTest = getTestClassByEntityName(result, "SkippedClassTest");
        assertThat(skippedClassTest.getResult()).isEqualTo(ExecutionResult.Skipped);
        assertThat(skippedClassTest.getSkipReason()).isNotEmpty();
        assertThat(skippedClassTest.getException()).isEmpty();
        assertThat(skippedClassTest.hasChildren()).isFalse();

        var with2SuccessfulTest = getTestClassByEntityName(result, "With2SuccessfulTest");
        assertThat(with2SuccessfulTest.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(with2SuccessfulTest.getSkipReason()).isEmpty();
        assertThat(with2SuccessfulTest.getException()).isEmpty();
        assertThat(with2SuccessfulTest.hasChildren()).isTrue();

        var with1FailingAnd1SuccessfulTest = getTestClassByEntityName(result, "With1FailingAnd1SuccessfulTest");
        assertThat(with1FailingAnd1SuccessfulTest.getSkipReason()).isEmpty();
        assertThat(with1FailingAnd1SuccessfulTest.hasChildren()).isTrue();
        //TODO: maybe it's better to propagate the child error to the container
        assertThat(with1FailingAnd1SuccessfulTest.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(with1FailingAnd1SuccessfulTest.getException()).isEmpty();

        var with2SkippedAnd1SuccessfulTest = getTestClassByEntityName(result, "With2SkippedAnd1SuccessfulTest");
        assertThat(with2SkippedAnd1SuccessfulTest.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(with2SkippedAnd1SuccessfulTest.getSkipReason()).isEmpty();
        assertThat(with2SkippedAnd1SuccessfulTest.getException()).isEmpty();
        assertThat(with2SkippedAnd1SuccessfulTest.hasChildren()).isTrue();
    }

    @Test
    void Given_a_successful_run_then_result_for_With2SkippedAnd1SuccessfulTest_children_is_as_expected() {
        var sut = buildTestRunnerForSubProject();

        var testClass = getTestClassByEntityName(sut.run().getTestContainers(), "With2SkippedAnd1SuccessfulTest");

        assertThat(testClass.getChildren())
                .hasSize(3)
                .allSatisfy(e -> {
                    assertThat(e.getType()).isEqualTo(TestEntityType.Method);
                    assertThat(e.hasChildren()).isFalse();
                    assertThat(e.getException()).isEmpty();
                });

        var firstSkippedTest = getTestMethodByEntityName(testClass, "it_runs_the_test()");
        assertThat(firstSkippedTest.getResult()).isEqualTo(ExecutionResult.Skipped);
        assertThat(firstSkippedTest.getSkipReason()).contains("reason");

        var secondSkippedTest = getTestMethodByEntityName(testClass, "it_runs_the_test_2()");
        assertThat(secondSkippedTest.getResult()).isEqualTo(ExecutionResult.Skipped);
        assertThat(secondSkippedTest.getSkipReason()).isNotEmpty();

        var successfulTest = getTestMethodByEntityName(testClass, "it_runs_the_test_3()");
        assertThat(successfulTest.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(successfulTest.getSkipReason()).isEmpty();
    }

    @Test
    void Given_a_successful_run_then_result_for_With1FailingAnd1SuccessfulTest_children_is_as_expected() {
        var sut = buildTestRunnerForSubProject();

        var testClass = getTestClassByEntityName(sut.run().getTestContainers(), "With1FailingAnd1SuccessfulTest");

        assertThat(testClass.getChildren())
                .hasSize(2)
                .allSatisfy(e -> {
                    assertThat(e.getType()).isEqualTo(TestEntityType.Method);
                    assertThat(e.hasChildren()).isFalse();
                    assertThat(e.getSkipReason()).isEmpty();
                });

        var successTest = getTestMethodByEntityName(testClass, "it_runs_the_test()");
        assertThat(successTest.getResult()).isEqualTo(ExecutionResult.Success);
        assertThat(successTest.getException()).isEmpty();

        var failingTest = getTestMethodByEntityName(testClass, "it_fails()");
        assertThat(failingTest.getResult()).isEqualTo(ExecutionResult.Failure);
        assertThat(failingTest.getException())
                .isNotEmpty()
                .containsInstanceOf(RuntimeException.class);
        assertThat(failingTest.getException().get().getMessage()).isEqualTo("purposely failing");
    }

    @Test
    void Given_a_successful_run_then_result_for_With2SuccessfulTest_children_is_as_expected() {
        var sut = buildTestRunnerForSubProject();

        var testClass = getTestClassByEntityName(sut.run().getTestContainers(), "With2SuccessfulTest");

        assertThat(testClass.getChildren()).hasSize(2);

        assertThat(testClass.getChildren())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        TestEntity.Success("success_1()", TestEntityType.Method, Collections.emptySet()),
                        TestEntity.Success("success_2()", TestEntityType.Method, Collections.emptySet())
                );
    }

    private static List<String> BuildGoalsList(String... goals) {
        var list = new ArrayList<String>();

        list.add("-Dsurefire.version=" + SUREFIRE_VERSION);
        list.add("-Dcompiler.version=" + COMPILER_VERSION);
        list.add("-q");

        list.addAll(Arrays.asList(goals));

        return list;
    }

    private static TestEntity getTestClassByEntityName(Set<TestEntity> result, String entityName) {
        return result.stream().filter(e -> e.getEntityName().equals(entityName)).findFirst().get();
    }

    private TestEntity getTestMethodByEntityName(TestEntity testClass, String testName) {
        return testClass.getChildren().stream().filter(e -> e.getEntityName().equals(testName)).findFirst().get();
    }

    private TestRunner buildTestRunnerForSubProject() {
        var options = new TestDiscoveryOptions(TestExecutionMode.ENTIRE_SUITE)
                .withAdditionalClassPaths(Set.of(getProjectClassPath()))
                .withIncludedPackageNames(Collections.singleton("src.test.java.it"));
        return testRunnerFactory.createTestRunner(options);
    }

    private Path getProjectClassPath() {
        return PROJECT_PATH.resolve("target/test-classes");
    }

    private static void BuildTestSubProject() throws MavenInvocationException {
        var pomPath = PROJECT_PATH.resolve("pom.xml");


        var invocationRequest = new DefaultInvocationRequest();
        invocationRequest.setPomFile(new File(pomPath.toUri()));
        invocationRequest.setBatchMode(true);
        invocationRequest.setGoals(BuildGoalsList("clean", "test-compile"));

        var invoker = new DefaultInvoker();
        var result = invoker.execute(invocationRequest);

        if (result.getExitCode() != 0) {
            throw new RuntimeException("maven error");
        }
    }
}
