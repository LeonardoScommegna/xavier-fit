package com.fteotini.Xavier.mutator;

import com.fteotini.Xavier.commons.util.SerializableFunction;
import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;

public final class Mutator implements IMutator {
    private static final long serialVersionUID = 42L;

    private final IMutationTarget mutationDetails;
    private final SerializableFunction<ByteBuddy, ? extends Operator> operatorFactory;
    private final String uniqueMutationOperationId;

    Mutator(String uniqueMutationOperationId, IMutationTarget mutationDetails, SerializableFunction<ByteBuddy, ? extends Operator> operatorFactory) {
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
