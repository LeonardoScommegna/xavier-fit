package com.fteotini.amf.mutator.Visitors;


import net.bytebuddy.jar.asm.AnnotationVisitor;

import java.util.function.Supplier;

public interface AnnotationVisitorWrapper {
    AnnotationVisitor wrap(Supplier<AnnotationVisitor> annotationVisitor, String descriptor, boolean visible);
}

