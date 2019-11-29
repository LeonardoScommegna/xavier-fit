package com.fteotini.Xavier.mutator.Operators.ForMethod;

import com.fteotini.Xavier.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.Xavier.mutator.Operators.Base.MethodOperator;
import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.Xavier.mutator.Visitors.ForMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;

import java.lang.annotation.Annotation;

public class RemoveAnnotationFromMethod<A extends Annotation> extends MethodOperator {
    private final Class<A> annotationType;

    protected RemoveAnnotationFromMethod(ByteBuddy byteBuddy, Class<A> annotationType) {
        super(byteBuddy);
        this.annotationType = annotationType;
    }

    @Override
    protected AsmVisitorWrapper visitor(MethodIdentifier identifier) {
        return new ForMethod(new RemoveAnnotation<>(annotationType), getMatcher(identifier));
    }
}
