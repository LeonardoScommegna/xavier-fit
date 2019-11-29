package com.fteotini.Xavier.mutator.Operators.ForClass;

import com.fteotini.Xavier.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.Xavier.mutator.Operators.Base.ClassOperator;
import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.ValuesReplacer.AnnotationValuesReplacer;
import com.fteotini.Xavier.mutator.Visitors.ForClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ReplaceClassAnnotationValues<K extends Annotation> extends ClassOperator {
    private final Class<K> annotationType;
    private final Map<String, Object> newValuesMap;

    public ReplaceClassAnnotationValues(ByteBuddy byteBuddy, Class<K> annotationType, Map<String, Object> newValuesMap) {
        super(byteBuddy);
        this.annotationType = annotationType;
        this.newValuesMap = newValuesMap;
    }

    @Override
    protected DynamicType.Builder<?> decorateBuilder(DynamicType.Builder<?> builder, ClassIdentifier identifier) {
        return builder.visit(new ForClass(new AnnotationValuesReplacer<>(annotationType, newValuesMap)));
    }
}
