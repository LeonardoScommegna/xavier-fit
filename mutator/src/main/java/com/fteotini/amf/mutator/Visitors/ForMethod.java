package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.ForAnnotations.AnnotationVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.pool.TypePool;

import static net.bytebuddy.matcher.ElementMatchers.hasDescriptor;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class ForMethod extends BaseVisitorWrapper {

    private final ElementMatcher<? super MethodDescription> matcher;

    ForMethod(AnnotationVisitorWrapper annotationVisitorWrapper, ElementMatcher<? super MethodDescription> matcher) {
        super(annotationVisitorWrapper);
        this.matcher = matcher;
    }

    @Override
    public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
        return new DispatchingVisitor(classVisitor, methods);
    }

    private class DispatchingVisitor extends ClassVisitor {

        private final MethodList<?> methods;

        DispatchingVisitor(ClassVisitor classVisitor, MethodList<?> methods) {
            super(ASM_VERSION, classVisitor);
            this.methods = methods;
        }

        @SuppressWarnings("DuplicatedCode")
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            var visitor = super.visitMethod(access, name, descriptor, signature, exceptions);

            var currentMethod = methods.filter(named(name).and(hasDescriptor(descriptor))).getOnly();
            if (visitor != null && matcher.matches(currentMethod)) {
                visitor = new MethodVisitor(ASM_VERSION, visitor) {
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
