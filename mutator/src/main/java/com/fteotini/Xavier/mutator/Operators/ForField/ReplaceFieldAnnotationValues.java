package com.fteotini.Xavier.mutator.Operators.ForField;

import com.fteotini.Xavier.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.Xavier.mutator.Operators.Base.FieldOperator;
import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.ValuesReplacer.AnnotationValuesReplacer;
import com.fteotini.Xavier.mutator.Visitors.ForField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ReplaceFieldAnnotationValues<K extends Annotation> extends FieldOperator {
    private final Class<K> annotationType;
    private final Map<String, Object> newValuesMap;

    ReplaceFieldAnnotationValues(ByteBuddy byteBuddy, Class<K> annotationType, Map<String, Object> newValuesMap) {
        super(byteBuddy);
        this.annotationType = annotationType;
        this.newValuesMap = newValuesMap;
    }

    @Override
    protected AsmVisitorWrapper visitor(FieldIdentifier identifier) {
        return new ForField(new AnnotationValuesReplacer<>(annotationType, newValuesMap), getMatcher(identifier));
    }
}
