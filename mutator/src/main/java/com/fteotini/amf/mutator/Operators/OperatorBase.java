package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.Identifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;

abstract class OperatorBase<T extends Identifier> implements AutoCloseable, Closeable {
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


    public final void runMutation(MutationDetailsInterface mutation) {
        getMutationTarget(mutation).ifPresent(identifier -> {
            mutantClass = getClassObject(className(identifier));
            byteBuddy.decorate(mutantClass)
                    .visit(visitor(identifier))
                    .make()
                    .load(mutantClass.getClassLoader(), classLoadingStrategy);
        });
    }

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

    protected abstract AsmVisitorWrapper visitor(T identifier);

    protected abstract String className(T identifier);

    protected abstract Optional<T> getMutationTarget(MutationDetailsInterface mutationDetailsInterface);

}
