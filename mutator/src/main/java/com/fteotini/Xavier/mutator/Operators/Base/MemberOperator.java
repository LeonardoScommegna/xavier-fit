package com.fteotini.Xavier.mutator.Operators.Base;

import com.fteotini.Xavier.mutator.MutationIdentifiers.Identifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatcher;

abstract class MemberOperator<T extends Identifier, K> extends OperatorBase<T> {

    MemberOperator(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    /**
     * For test purpose
     *
     * @param byteBuddy
     * @param classLoadingStrategy
     */
    MemberOperator(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        super(byteBuddy, classLoadingStrategy);
    }

    @Override
    protected final DynamicType.Builder<?> decorateBuilder(DynamicType.Builder<?> builder, T identifier) {
        return builder.visit(visitor(identifier));
    }

    protected abstract ElementMatcher<? super K> getMatcher(T identifier);

    protected abstract AsmVisitorWrapper visitor(T identifier);
}
