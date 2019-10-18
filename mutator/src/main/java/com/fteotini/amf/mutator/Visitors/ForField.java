package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.AnnotationVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.pool.TypePool;

import static net.bytebuddy.matcher.ElementMatchers.hasDescriptor;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class ForField extends BaseClassVisitorWrapper {

    private final ElementMatcher<? super FieldDescription.InDefinedShape> matcher;

    public ForField(AnnotationVisitorWrapper annotationVisitorWrapper, ElementMatcher<? super FieldDescription.InDefinedShape> matcher) {
        super(annotationVisitorWrapper);
        this.matcher = matcher;
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
        return new DispatcherVisitor(classVisitor, fields);
    }

    private class DispatcherVisitor extends ClassVisitor {
        private final FieldList<FieldDescription.InDefinedShape> fields;

        DispatcherVisitor(ClassVisitor classVisitor, FieldList<FieldDescription.InDefinedShape> fields) {
            super(AsmConfig.ASM_VERSION, classVisitor);
            this.fields = fields;
        }

        @SuppressWarnings("DuplicatedCode")
        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            var visitor = super.visitField(access, name, descriptor, signature, value);

            var fieldDescription = fields.filter(named(name).and(hasDescriptor(descriptor))).getOnly();
            if (visitor != null && matcher.matches(fieldDescription)) {
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
