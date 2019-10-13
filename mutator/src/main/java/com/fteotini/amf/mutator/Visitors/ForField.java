package com.fteotini.amf.mutator.Visitors;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.pool.TypePool;

public class ForField implements AsmVisitorWrapper {

    private final AnnotationVisitorWrapper annotationVisitorWrapper;

    public ForField(AnnotationVisitorWrapper annotationVisitorWrapper) {
        this.annotationVisitorWrapper = annotationVisitorWrapper;
    }

    @Override
    public int mergeWriter(int flags) {
        return flags;
    }

    @Override
    public int mergeReader(int flags) {
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
        return new DispatcherVisitor(classVisitor);
    }

    private class DispatcherVisitor extends ClassVisitor {
        DispatcherVisitor(ClassVisitor classVisitor) {
            super(AsmConfig.ASM_VERSION, classVisitor);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            var visitor = super.visitField(access, name, descriptor, signature, value);
            if (visitor != null) {
                visitor = new FieldVisitor(AsmConfig.ASM_VERSION, visitor) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        return annotationVisitorWrapper.wrap(() -> super.visitAnnotation(descriptor, visible), descriptor, visible);
                    }
                };
            }

            return visitor;
        }
    }
}
