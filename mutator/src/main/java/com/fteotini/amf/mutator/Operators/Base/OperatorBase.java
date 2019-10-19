package com.fteotini.amf.mutator.Operators.Base;

import com.fteotini.amf.mutator.IMutationTarget;
import com.fteotini.amf.mutator.MutationIdentifiers.Identifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.io.IOException;
import java.util.Optional;

abstract class OperatorBase<T extends Identifier> implements Operator {
    private final ByteBuddy byteBuddy;
    private final ClassReloadingStrategy classLoadingStrategy;

    private Class<?> mutantClass;

    OperatorBase(final ByteBuddy byteBuddy) {
        this(byteBuddy, ClassReloadingStrategy.fromInstalledAgent());
    }

    /**
     * For test purpose
     */
    OperatorBase(final ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
        this.byteBuddy = byteBuddy;
        this.classLoadingStrategy = classLoadingStrategy;
    }


    @Override
    public final void runMutation(IMutationTarget mutation) {
        getMutationTarget(mutation).ifPresent(identifier -> {
            mutantClass = getClassObject(className(identifier));
            var builder = byteBuddy.decorate(mutantClass);

            builder = decorateBuilder(builder,identifier);

            builder
                    .make()
                    .load(mutantClass.getClassLoader(), classLoadingStrategy);
        });
    }

    protected abstract DynamicType.Builder<?> decorateBuilder(DynamicType.Builder<?> builder, T identifier);

    @Override
    public void close() throws IOException {
        classLoadingStrategy.reset(mutantClass);
    }

    protected static Class<?> getClassObject(String fullName) {
        try {
            return Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String className(T identifier);

    protected abstract Optional<T> getMutationTarget(IMutationTarget mutationDetailsInterface);

}
