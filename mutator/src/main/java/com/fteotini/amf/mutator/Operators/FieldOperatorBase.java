package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.Visitors.ForField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;

abstract class FieldOperatorBase extends OperatorBase<FieldIdentifier, ForField> {
    public FieldOperatorBase(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    FieldOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, classLoadingStrategy);
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
