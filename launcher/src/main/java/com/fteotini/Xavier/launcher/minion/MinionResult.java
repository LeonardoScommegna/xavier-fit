package com.fteotini.Xavier.launcher.minion;

import org.zeroturnaround.exec.ProcessResult;

import java.util.List;

public class MinionResult {
    private final ProcessResult processResult;
    private final List<MutationResult> mutationResults;

    public MinionResult(final ProcessResult processResult, final List<MutationResult> mutationResults) {
        this.processResult = processResult;
        this.mutationResults = mutationResults;
    }

    public ProcessResult getProcessResult() {
        return processResult;
    }

    public List<MutationResult> getMutationResults() {
        return mutationResults;
    }
}
