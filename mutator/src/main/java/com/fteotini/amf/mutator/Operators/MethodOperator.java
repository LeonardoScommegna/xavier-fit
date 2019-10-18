package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.Arrays;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

abstract class MethodOperator extends MemberOperator<MethodIdentifier, MethodDescription> {

    MethodOperator(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    MethodOperator(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
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

    private static Class<?>[] getParametersClass(String[] classNames) {
        return Arrays.stream(classNames)
                .map(OperatorBase::getClassObject)
                .toArray(Class<?>[]::new);
    }

    @Override
    protected final ElementMatcher<? super MethodDescription> getMatcher(MethodIdentifier identifier) {
        return named(identifier.getName()).and(takesArguments(getParametersClass(identifier.getParametersType())));
    }
}
