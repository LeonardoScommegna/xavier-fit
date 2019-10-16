package com.fteotini.amf.mutator.Visitors;

import com.fteotini.amf.mutator.Visitors.ForAnnotations.AnnotationVisitorWrapper;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.jar.asm.Opcodes;

abstract class BaseVisitorWrapper implements AsmVisitorWrapper {
    static final int ASM_VERSION = Opcodes.ASM7;

    final AnnotationVisitorWrapper annotationVisitorWrapper;

    BaseVisitorWrapper(AnnotationVisitorWrapper annotationVisitorWrapper) {
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
