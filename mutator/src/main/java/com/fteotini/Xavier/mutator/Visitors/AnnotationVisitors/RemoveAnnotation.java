package com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors;

import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.Type;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

public class RemoveAnnotation<K extends Annotation> implements AnnotationVisitorWrapper {

    private final String annotationDescriptor;

    public RemoveAnnotation(Class<K> annotationType) {
        annotationDescriptor = Type.getDescriptor(annotationType);
    }

    @Override
    public AnnotationVisitor wrap(Supplier<AnnotationVisitor> annotationVisitor, String descriptor, boolean visible) {
        if (descriptor.equals(annotationDescriptor)) {
            return null;
        } else {
            return annotationVisitor.get();
        }
    }
}
