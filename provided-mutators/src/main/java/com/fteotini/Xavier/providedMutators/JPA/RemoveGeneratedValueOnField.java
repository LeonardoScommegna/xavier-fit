package com.fteotini.Xavier.providedMutators.JPA;

import com.fteotini.Xavier.mutator.MutatorsBuilderBase;
import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import com.fteotini.Xavier.mutator.Operators.Finders.FieldOperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.Finders.OperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.ForField.RemoveAnnotationFromField;
import net.bytebuddy.ByteBuddy;

import javax.persistence.GeneratedValue;
import java.lang.annotation.Annotation;

public class RemoveGeneratedValueOnField extends MutatorsBuilderBase {
    @Override
    protected OperatorTargetsFinder targetsFinder() {
        return new FieldOperatorTargetsFinder();
    }

    @Override
    protected Class<? extends Annotation> currentAnnotation() {
        return GeneratedValue.class;
    }

    @Override
    protected Operator operator(ByteBuddy buddy) {
        return new RemoveAnnotationFromField<>(buddy, currentAnnotation());
    }

    @Override
    protected String mutationOperationId() {
        return "[REMOVE][FIELD] @GeneratedValue";
    }
}
