package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.AnnotationVisitorWrapper;
import net.bytebuddy.asm.AsmVisitorWrapper;

abstract class BaseClassVisitorWrapper implements AsmVisitorWrapper {

    final AnnotationVisitorWrapper annotationVisitorWrapper;

    BaseClassVisitorWrapper(AnnotationVisitorWrapper annotationVisitorWrapper) {
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
}
