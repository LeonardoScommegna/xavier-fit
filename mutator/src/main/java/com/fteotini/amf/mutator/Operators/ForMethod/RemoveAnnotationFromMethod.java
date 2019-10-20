package com.fteotini.amf.mutator.Operators.ForMethod;

import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.amf.mutator.Operators.Base.MethodOperator;
import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.amf.mutator.Visitors.ForMethod;
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
