package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;

import java.util.function.Function;

public final class Mutator implements IMutator {
    private static final long serialVersionUID = 42L;

    private final IMutationTarget mutationDetails;
    private final Function<ByteBuddy, ? extends Operator> operatorFactory;
    private final String uniqueMutationOperationId;

    Mutator(String uniqueMutationOperationId, IMutationTarget mutationDetails, Function<ByteBuddy, ? extends Operator> operatorFactory) {
        this.mutationDetails = mutationDetails;
        this.operatorFactory = operatorFactory;
        this.uniqueMutationOperationId = uniqueMutationOperationId;
    }

    @Override
    public IMutationTarget getMutationDetails() {
        return mutationDetails;
    }

    @Override
    public String getUniqueMutationOperationId() {
        return uniqueMutationOperationId;
    }

    @Override
    public Operator makeOperator(ByteBuddy buddy) {
        return operatorFactory.apply(buddy);
    }
}
