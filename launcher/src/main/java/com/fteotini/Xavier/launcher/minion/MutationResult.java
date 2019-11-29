package com.fteotini.Xavier.launcher.minion;

import com.fteotini.Xavier.commons.tester.ExecutionSummary.ExecutionResult;
import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestEntity;
import com.fteotini.Xavier.mutator.IMutationTarget;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

public class MutationResult implements Serializable {
    private static final long serialVersionUID = 42L;

    private final IMutationTarget mutationTarget;
    private final String uniqueMutationOperationId;
    private final Set<TestEntity> testContainers;

    MutationResult(IMutationTarget mutationTarget, String uniqueMutationOperationId, Set<TestEntity> testContainers) {
        this.mutationTarget = mutationTarget;
        this.uniqueMutationOperationId = uniqueMutationOperationId;
        this.testContainers = testContainers;
    }

    public Set<TestEntity> getTestContainers() {
        return testContainers;
    }

    public IMutationTarget getMutationTarget() {
        return mutationTarget;
    }

    public String getUniqueMutationOperationId() {
        return uniqueMutationOperationId;
    }

    public boolean isSurvived() {
        var isDead = false;

        Queue<TestEntity> entitiesQueue = new ArrayDeque<>(testContainers);
        while (!entitiesQueue.isEmpty() && !isDead) {
            var entity = entitiesQueue.poll();
            entitiesQueue.addAll(entity.getChildren());

            isDead = entity.getResult() == ExecutionResult.Failure;
        }

        return !isDead;
    }
}
