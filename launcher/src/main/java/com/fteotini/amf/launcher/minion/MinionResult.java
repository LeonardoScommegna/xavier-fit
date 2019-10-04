package com.fteotini.amf.launcher.minion;

import com.fteotini.amf.commons.tester.ExecutionSummary.TestExecutionSummary;
import org.zeroturnaround.exec.ProcessResult;

public class MinionResult {
    private final ProcessResult processResult;
    private final TestExecutionSummary testExecutionSummary;

    public MinionResult(final ProcessResult processResult, final TestExecutionSummary testExecutionSummary) {
        this.processResult = processResult;
        this.testExecutionSummary = testExecutionSummary;
    }

    public ProcessResult getProcessResult() {
        return processResult;
    }

    public TestExecutionSummary getTestExecutionSummary() {
        return testExecutionSummary;
    }
}
