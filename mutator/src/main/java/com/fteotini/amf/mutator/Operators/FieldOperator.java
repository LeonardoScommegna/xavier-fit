package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;
import java.util.function.Function;

class FieldOperator extends OperatorBase<FieldIdentifier> {

    FieldOperator(ByteBuddy byteBuddy, Function<FieldIdentifier, AsmVisitorWrapper> visitorFactory) {
        super(byteBuddy, visitorFactory);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param visitorFactory
     * @param classLoadingStrategy
     */
    FieldOperator(ByteBuddy byteBuddy, Function<FieldIdentifier, AsmVisitorWrapper> visitorFactory, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, visitorFactory, classLoadingStrategy);
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
