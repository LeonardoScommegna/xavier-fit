package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.ForAnnotations.AnnotationVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.pool.TypePool;

public class ForClass extends BaseClassVisitorWrapper {

    ForClass(AnnotationVisitorWrapper annotationVisitorWrapper) {
        super(annotationVisitorWrapper);
    }

    @Override
    public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        return new DispatchingVisitor(classVisitor);
    }

    private class DispatchingVisitor extends ClassVisitor {
        DispatchingVisitor(ClassVisitor parentVisitor) {
            super(AsmConfig.ASM_VERSION, parentVisitor);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            return annotationVisitorWrapper.wrap(() -> super.visitAnnotation(descriptor, visible), descriptor, visible);
        }
    }
}
