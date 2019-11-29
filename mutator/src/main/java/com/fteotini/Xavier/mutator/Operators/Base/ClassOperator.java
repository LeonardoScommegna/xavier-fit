package com.fteotini.Xavier.mutator.Operators.Base;

import com.fteotini.Xavier.mutator.IMutationTarget;
import com.fteotini.Xavier.mutator.MutationIdentifiers.ClassIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.util.Optional;

public abstract class ClassOperator extends OperatorBase<ClassIdentifier> {
    public ClassOperator(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    ClassOperator(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, classLoadingStrategy);
    }

    @Override
    protected final String className(ClassIdentifier identifier) {
        return identifier.getName();
    }

    @Override
    protected final Optional<ClassIdentifier> getIdentifier(IMutationTarget mutationDetailsInterface) {
        return mutationDetailsInterface.getClassIdentifier();
    }

}
