package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotFinishedYetException;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotStartedYetException;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSuiteOutcomeGeneratingListener implements TestExecutionListener {
    private final TestSuiteOutcomeBuilder builder;
    private boolean isStarted = false;
    private boolean isFinished = false;


    private final Map<String,TestNode> testsNodesByUniqueId = new ConcurrentHashMap<>();
    private final TestNode root = new TestNode(null);

    public TestSuiteOutcomeGeneratingListener(TestSuiteOutcomeBuilder builder) {
        this.builder = builder;
    }

    public TestSuiteOutcome generateTestSuiteOutcome(){
        if (!isStarted)
            throw new TestSuiteNotStartedYetException();
        if (!isFinished)
            throw new TestSuiteNotFinishedYetException();

        return builder.build(root);
    }

    /**
     * Called when the execution of the {@link TestPlan} has started,
     * <em>before</em> any test has been executed.
     *
     * @param testPlan describes the tree of tests about to be executed
     */
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        isStarted = true;
    }

    /**
     * Called when the execution of the {@link TestPlan} has finished,
     * <em>after</em> all tests have been executed.
     *
     * @param testPlan describes the tree of tests that have been executed
     */
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        if (!isStarted) {
            throw new TestSuiteNotStartedYetException();
        }
        isFinished = true;
    }

    /**
     * Called when the execution of a leaf or subtree of the {@link TestPlan}
     * is about to be started.
     *
     * <p>The {@link TestIdentifier} may represent a test or a container.
     *
     * <p>This method will only be called if the test or container has not
     * been {@linkplain #executionSkipped skipped}.
     *
     * <p>This method will be called for a container {@code TestIdentifier}
     * <em>before</em> {@linkplain #executionStarted starting} or
     * {@linkplain #executionSkipped skipping} any of its children.
     *
     * @param testIdentifier the identifier of the started test or container
     */
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        var node = new TestNode(testIdentifier);
        testsNodesByUniqueId.put(testIdentifier.getUniqueId(),node);

        testIdentifier.getParentId().map(testsNodesByUniqueId::get).orElse(root).addChild(node);
    }
}
