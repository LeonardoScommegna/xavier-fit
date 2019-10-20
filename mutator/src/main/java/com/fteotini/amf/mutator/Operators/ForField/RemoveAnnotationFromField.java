package com.fteotini.amf.mutator.Operators.ForField;

import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.Operators.Base.FieldOperator;
import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.amf.mutator.Visitors.ForField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;

import java.lang.annotation.Annotation;

public class RemoveAnnotationFromField<K extends Annotation> extends FieldOperator {
    private final Class<K> annotationClass;

    RemoveAnnotationFromField(ByteBuddy byteBuddy, Class<K> annotationClass) {
        super(byteBuddy);
        this.annotationClass = annotationClass;
    }

    @Override
    protected AsmVisitorWrapper visitor(FieldIdentifier identifier) {
        return new ForField(new RemoveAnnotation<>(annotationClass), getMatcher(identifier));
    }
}
