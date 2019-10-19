package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;

import java.io.Serializable;
import java.util.function.Function;

public final class Mutator implements Serializable {
    private static final long serialVersionUID = 42L;

    private final IMutationTarget mutationDetails;
    private final Function<ByteBuddy, ? extends Operator> operatorFactory;
    private final String uniqueMutationOperationId;

    Mutator(String uniqueMutationOperationId, IMutationTarget mutationDetails, Function<ByteBuddy, ? extends Operator> operatorFactory) {
        this.mutationDetails = mutationDetails;
        this.operatorFactory = operatorFactory;
        this.uniqueMutationOperationId = uniqueMutationOperationId;
    }

    public IMutationTarget getMutationDetails() {
        return mutationDetails;
    }

    public String getUniqueMutationOperationId() {
        return uniqueMutationOperationId;
    }

    public void runMutation(ByteBuddy buddy) {
        operatorFactory.apply(buddy).runMutation(mutationDetails);
    }
}
