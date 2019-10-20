package com.fteotini.amf.providedMutators.JPA;

import com.fteotini.amf.mutator.MutatorsBuilderBase;
import com.fteotini.amf.mutator.Operators.Base.Operator;
import com.fteotini.amf.mutator.Operators.Finders.FieldOperatorTargetsFinder;
import com.fteotini.amf.mutator.Operators.Finders.OperatorTargetsFinder;
import com.fteotini.amf.mutator.Operators.ForField.RemoveAnnotationFromField;
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
