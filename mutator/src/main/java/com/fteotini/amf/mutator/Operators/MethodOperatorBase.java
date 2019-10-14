package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.amf.mutator.Visitors.ForMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;

abstract class MethodOperatorBase extends OperatorBase<MethodIdentifier, ForMethod> {
    public MethodOperatorBase(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    MethodOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, classLoadingStrategy);
    }

    @Override
    protected final String className(MethodIdentifier identifier) {
        return identifier.getBelongingClass().getName();
    }

    @Override
    protected final Optional<MethodIdentifier> getMutationTarget(MutationDetailsInterface mutationDetailsInterface) {
        return mutationDetailsInterface.getMethodIdentifier();
    }
}
