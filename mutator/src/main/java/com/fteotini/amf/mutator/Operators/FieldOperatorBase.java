package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import net.bytebuddy.ByteBuddy;

import java.util.Optional;

abstract class FieldOperatorBase extends OperatorBase<FieldIdentifier> {
    public FieldOperatorBase(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    @Override
    protected final String className(FieldIdentifier identifier) {
        return identifier.getBelongingClass().getName();
    }

    @Override
    protected final Optional<FieldIdentifier> getMutationTarget(MutationDetailsInterface mutationDetailsInterface) {
        return mutationDetailsInterface.getFieldIdentifier();
    }
}
