package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.Visitors.ForClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;

abstract class ClassOperatorBase extends OperatorBase<ClassIdentifier, ForClass> {
    ClassOperatorBase(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    ClassOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, classLoadingStrategy);
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
