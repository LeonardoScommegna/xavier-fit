package com.fteotini.Xavier.mutator.Operators.ForClass;

import com.fteotini.Xavier.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.Xavier.mutator.Operators.Base.ClassOperator;
import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.Xavier.mutator.Visitors.ForClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.annotation.Annotation;

public class SwitchClassAnnotation<S extends Annotation, T extends Annotation> extends ClassOperator {
    private final Class<S> srcAnnotation;
    private final Class<T> trgAnnotation;

    public SwitchClassAnnotation(ByteBuddy byteBuddy, Class<S> srcAnnotation, Class<T> trgAnnotation) {
        super(byteBuddy);
        this.srcAnnotation = srcAnnotation;
        this.trgAnnotation = trgAnnotation;
    }

    @Override
    protected DynamicType.Builder<?> decorateBuilder(DynamicType.Builder<?> builder, ClassIdentifier identifier) {
        return builder
                .visit(new ForClass(new RemoveAnnotation<>(srcAnnotation)))
                .annotateType(AnnotationDescription.Builder.ofType(trgAnnotation).build());
    }
}
