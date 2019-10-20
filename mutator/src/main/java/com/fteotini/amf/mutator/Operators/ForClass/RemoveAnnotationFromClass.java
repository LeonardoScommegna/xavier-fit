package com.fteotini.amf.mutator.Operators.ForClass;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.Operators.Base.ClassOperator;
import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.amf.mutator.Visitors.ForClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.annotation.Annotation;

public class RemoveAnnotationFromClass<A extends Annotation> extends ClassOperator {
    private final Class<A> annotationClass;

    public RemoveAnnotationFromClass(ByteBuddy byteBuddy, Class<A> annotationClass) {
        super(byteBuddy);
        this.annotationClass = annotationClass;
    }

    @Override
    protected DynamicType.Builder<?> decorateBuilder(DynamicType.Builder<?> builder, ClassIdentifier identifier) {
        return builder.visit(new ForClass(new RemoveAnnotation<>(annotationClass)));
    }
}
