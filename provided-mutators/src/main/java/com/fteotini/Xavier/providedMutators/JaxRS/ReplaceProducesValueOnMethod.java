package com.fteotini.Xavier.providedMutators.JaxRS;

import com.fteotini.Xavier.mutator.MutatorsBuilderBase;
import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import com.fteotini.Xavier.mutator.Operators.Finders.MethodOperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.Finders.OperatorTargetsFinder;
import com.fteotini.Xavier.mutator.Operators.ForMethod.ReplaceMethodAnnotationValues;
import net.bytebuddy.ByteBuddy;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.util.Map;

public class ReplaceProducesValueOnMethod extends MutatorsBuilderBase {
    @Override
    protected OperatorTargetsFinder targetsFinder() {
        return new MethodOperatorTargetsFinder();
    }

    @Override
    protected Class<? extends Annotation> currentAnnotation() {
        return Produces.class;
    }

    @Override
    protected Operator operator(ByteBuddy buddy) {
        return new ReplaceMethodAnnotationValues<>(buddy, currentAnnotation(), Map.of("value", new String[]{MediaType.TEXT_HTML}));
    }

    @Override
    protected String mutationOperationId() {
        return "[CHANGE][METHOD] @Produces";
    }
}
