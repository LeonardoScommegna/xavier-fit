package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.AnnotationVisitorWrapper;
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

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ForMethod extends BaseClassVisitorWrapper {

    private final ElementMatcher<? super MethodDescription> matcher;

    public ForMethod(AnnotationVisitorWrapper annotationVisitorWrapper, ElementMatcher<? super MethodDescription> matcher) {
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
            super(AsmConfig.ASM_VERSION, classVisitor);
            this.methods = methods;
        }

        @SuppressWarnings("DuplicatedCode")
        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            var visitor = super.visitMethod(access, name, descriptor, signature, exceptions);

            var currentMethod = getCurrentVisitedMethod(name, descriptor);
            if (visitor != null && matcher.matches(currentMethod)) {
                visitor = new MethodVisitor(AsmConfig.ASM_VERSION, visitor) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        return annotationVisitorWrapper.wrap(() -> super.visitAnnotation(descriptor, visible), descriptor, visible);
                    }
                };
            }

            return visitor;
        }

        private MethodDescription getCurrentVisitedMethod(String name, String descriptor) {
            MethodList<?> filtered;
            if (name.equals("<init>")) {
                filtered = methods.filter(isConstructor().and(hasDescriptor(descriptor)));
            } else {
                filtered = methods.filter(named(name).and(hasDescriptor(descriptor)));
            }
            return filtered.getOnly();
        }
    }
}
