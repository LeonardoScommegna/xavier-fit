package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import net.bytebuddy.ByteBuddy;

import java.util.Optional;

abstract class ClassOperatorBase extends OperatorBase<ClassIdentifier> {
    public ClassOperatorBase(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    @Override
    protected final String className(ClassIdentifier identifier) {
        return identifier.getName();
    }

    @Override
    protected final Optional<ClassIdentifier> getMutationTarget(MutationDetails mutationDetails) {
        return mutationDetails.getClassIdentifier();
    }
}