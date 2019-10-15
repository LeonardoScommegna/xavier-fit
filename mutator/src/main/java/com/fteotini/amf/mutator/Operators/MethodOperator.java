package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;
import java.util.function.Function;

class MethodOperator extends OperatorBase<MethodIdentifier> {

    MethodOperator(ByteBuddy byteBuddy, Function<MethodIdentifier, AsmVisitorWrapper> visitorFactory) {
        super(byteBuddy, visitorFactory);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param visitorFactory
     * @param classLoadingStrategy
     */
    MethodOperator(ByteBuddy byteBuddy, Function<MethodIdentifier, AsmVisitorWrapper> visitorFactory, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, visitorFactory, classLoadingStrategy);
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
