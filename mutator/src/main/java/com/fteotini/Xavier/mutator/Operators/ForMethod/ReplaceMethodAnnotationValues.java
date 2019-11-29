package com.fteotini.Xavier.mutator.Operators.ForMethod;

import com.fteotini.Xavier.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.Xavier.mutator.Operators.Base.MethodOperator;
import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.ValuesReplacer.AnnotationValuesReplacer;
import com.fteotini.Xavier.mutator.Visitors.ForMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ReplaceMethodAnnotationValues<A extends Annotation> extends MethodOperator {
    private final Class<A> annotationType;
    private final Map<String, Object> newValuesMap;

    public ReplaceMethodAnnotationValues(ByteBuddy byteBuddy, Class<A> annotationType, Map<String, Object> newValuesMap) {
        super(byteBuddy);
        this.annotationType = annotationType;
        this.newValuesMap = newValuesMap;
    }

    @Override
    protected AsmVisitorWrapper visitor(MethodIdentifier identifier) {
        return new ForMethod(new AnnotationValuesReplacer<>(annotationType, newValuesMap), getMatcher(identifier));
    }
}
