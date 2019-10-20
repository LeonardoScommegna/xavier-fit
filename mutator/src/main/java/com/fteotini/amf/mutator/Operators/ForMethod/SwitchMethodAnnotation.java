package com.fteotini.amf.mutator.Operators.ForMethod;

import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.amf.mutator.Operators.Base.MethodOperator;
import com.fteotini.amf.mutator.Visitors.AnnotationVisitors.RemoveAnnotation;
import com.fteotini.amf.mutator.Visitors.Chain;
import com.fteotini.amf.mutator.Visitors.ForMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.annotation.AnnotationDescription;

import java.lang.annotation.Annotation;

public class SwitchMethodAnnotation<S extends Annotation, T extends Annotation> extends MethodOperator {
    private final Class<S> srcType;
    private final Class<T> trgType;

    protected SwitchMethodAnnotation(ByteBuddy byteBuddy, Class<S> srcType, Class<T> trgType) {
        super(byteBuddy);
        this.srcType = srcType;
        this.trgType = trgType;
    }

    @Override
    protected AsmVisitorWrapper visitor(MethodIdentifier identifier) {
        return new Chain(
                new ForMethod(new RemoveAnnotation<>(srcType), getMatcher(identifier)),
                new MemberAttributeExtension.ForMethod().annotateMethod(AnnotationDescription.Builder.ofType(trgType).build()).on(getMatcher(identifier))
        );
    }
}
