package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;
import java.util.function.Function;

class ClassOperator extends OperatorBase<ClassIdentifier> {

    ClassOperator(ByteBuddy byteBuddy, Function<ClassIdentifier, AsmVisitorWrapper> visitorFactory) {
        super(byteBuddy, visitorFactory);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param visitorFactory
     * @param classLoadingStrategy
     */
    ClassOperator(ByteBuddy byteBuddy, Function<ClassIdentifier, AsmVisitorWrapper> visitorFactory, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, visitorFactory, classLoadingStrategy);
    }

    @Override
    protected final String className(ClassIdentifier identifier) {
        return identifier.getName();
    }

    @Override
    protected final Optional<ClassIdentifier> getMutationTarget(MutationDetailsInterface mutationDetailsInterface) {
        return mutationDetailsInterface.getClassIdentifier();
    }
}
