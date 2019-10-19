package com.fteotini.amf.providedMutators.CDI;

import com.fteotini.amf.mutator.MutatorsBuilderBase;
import com.fteotini.amf.mutator.Operators.Base.Operator;
import com.fteotini.amf.mutator.Operators.Finders.ClassOperatorTargetsFinder;
import com.fteotini.amf.mutator.Operators.Finders.OperatorTargetsFinder;
import com.fteotini.amf.mutator.Operators.SwitchClassAnnotation;
import net.bytebuddy.ByteBuddy;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import java.lang.annotation.Annotation;

public class SessionToRequestScopedOnType extends MutatorsBuilderBase {
    @Override
    protected OperatorTargetsFinder targetsFinder() {
        return new ClassOperatorTargetsFinder();
    }

    @Override
    protected Class<? extends Annotation> currentAnnotation() {
        return SessionScoped.class;
    }

    @Override
    protected Operator operator(ByteBuddy buddy) {
        return new SwitchClassAnnotation<>(buddy, currentAnnotation(), RequestScoped.class);
    }

    @Override
    protected String mutationOperationId() {
        return "[TYPE Mutation] Switch @SessionScoped --> @RequestScoped";
    }
}
