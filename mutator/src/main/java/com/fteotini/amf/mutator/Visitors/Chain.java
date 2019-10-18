package com.fteotini.amf.mutator.Visitors;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.pool.TypePool;

import java.util.Arrays;
import java.util.List;

public class Chain implements AsmVisitorWrapper {
    private final List<AsmVisitorWrapper> wrappers;

    public Chain(AsmVisitorWrapper... wrappers) {
        this(Arrays.asList(wrappers));
    }

    public Chain(List<AsmVisitorWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public int mergeWriter(int flags) {
        for (AsmVisitorWrapper wrapper : wrappers) {
            flags = wrapper.mergeWriter(flags);
        }

        return flags;
    }

    @Override
    public int mergeReader(int flags) {
        for (AsmVisitorWrapper wrapper : wrappers) {
            flags = wrapper.mergeReader(flags);
        }

        return flags;
    }

    @Override
    public ClassVisitor wrap(
            TypeDescription instrumentedType,
            ClassVisitor classVisitor,
            Implementation.Context implementationContext,
            TypePool typePool,
            FieldList<FieldDescription.InDefinedShape> fields,
            MethodList<?> methods,
            int writerFlags,
            int readerFlags
    ) {
        for (AsmVisitorWrapper wrapper : wrappers) {
            classVisitor = wrapper.wrap(
                    instrumentedType,
                    classVisitor,
                    implementationContext,
                    typePool,
                    fields,
                    methods,
                    writerFlags,
                    readerFlags
            );
        }

        return classVisitor;
    }
}
