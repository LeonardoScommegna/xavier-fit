package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotFinishedYetException;
import com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator.exceptions.TestSuiteNotStartedYetException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestSuiteOutcomeGeneratingListener implements TestExecutionListener {
    private final Map<String, TestNode> testsNodesByUniqueId = new ConcurrentHashMap<>();
    private final TestNode root = TestNode.Root();
    private boolean isFinished = false;

    public TestSuiteOutcome generateTestSuiteOutcome() {
        if (!isFinished)
            throw new TestSuiteNotFinishedYetException();

        return null;
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        isFinished = true;
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        getTestNodeByIdentifier(testIdentifier).setResult(testExecutionResult);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        var node = getTestNodeByIdentifier(testIdentifier);
        if (node == null) {
            node = new TestNode(testIdentifier);
            addNode(testIdentifier, node);
        }

        node.setSkipReason(reason);
    }

    private TestNode getTestNodeByIdentifier(TestIdentifier testIdentifier) {
        return testsNodesByUniqueId.get(testIdentifier.getUniqueId());
    }

    private void addNode(TestIdentifier testIdentifier, TestNode node) {
        testsNodesByUniqueId.put(testIdentifier.getUniqueId(), node);
        testIdentifier.getParentId().map(testsNodesByUniqueId::get).orElse(root).addChild(node);
    }
}
