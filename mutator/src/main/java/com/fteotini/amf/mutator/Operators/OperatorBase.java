package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import com.fteotini.amf.mutator.MutationIdentifiers.Identifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

abstract class OperatorBase<T extends Identifier> implements Operator, AutoCloseable, Closeable {
    private final ByteBuddy byteBuddy;
    private final ClassReloadingStrategy classLoadingStrategy;
    private Class<?> classObject;

    OperatorBase(final ByteBuddy byteBuddy) {
        this.byteBuddy = byteBuddy;
        classLoadingStrategy = ClassReloadingStrategy.fromInstalledAgent();
    }

    @Override
    public void runMutation(MutationDetails mutation) {
        getMutationTarget(mutation).ifPresent(identifier -> {
            classObject = getClassObject(className(identifier));
            byteBuddy.decorate(classObject)
                    .visit(visitor(identifier))
                    .make()
                    .load(classObject.getClassLoader(), classLoadingStrategy);
        });
    }

    @Override
    public void close() throws IOException {
        classLoadingStrategy.reset(classObject);
    }

    private static Class<?> getClassObject(String fullName) {
        try {
            return Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] getParametersClass(String[] classNames) {
        return Arrays.stream(classNames)
                .map(OperatorBase::getClassObject)
                .toArray(Class<?>[]::new);
    }

    protected abstract String className(T identifier);

    protected abstract Optional<T> getMutationTarget(MutationDetails mutationDetails);

    protected abstract AsmVisitorWrapper visitor(T targetIdentifier);
}
