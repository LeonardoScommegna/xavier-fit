package com.fteotini.Xavier.providedMutators.CDI;

import com.fteotini.Xavier.mutator.MutatorsBuilderBase;
import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import com.fteotini.Xavier.mutator.Operators.Finders.ClassOperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.Finders.OperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.ForClass.SwitchClassAnnotation;
import net.bytebuddy.ByteBuddy;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import java.lang.annotation.Annotation;

public class SwitchSessionToRequestScopedOnType extends MutatorsBuilderBase {
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
        return "[SWITCH][TYPE] @SessionScoped --> @RequestScoped";
    }
}
