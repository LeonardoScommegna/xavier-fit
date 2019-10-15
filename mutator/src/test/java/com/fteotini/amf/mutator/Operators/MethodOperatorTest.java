package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class MethodOperatorTest extends AbstractOperatorTest<MethodIdentifier> {
    @Test
    void Given_a_mutationDetails_it_will_select_the_fieldIdentifier_and_return_the_belonging_class_name() {
        var clazz = getClass();
        var className = clazz.getCanonicalName();
        var identifier = new MethodIdentifier("dd", new String[0], new ClassIdentifier(className));
        when(mutationDetails.getMethodIdentifier()).thenReturn(Optional.of(identifier));

        sut.runMutation(mutationDetails);

        verify(buddy).decorate(clazz);
    }

    @Override
    protected OperatorBase<MethodIdentifier> buildSut() {
        return new MethodOperator(buddy, methodIdentifier -> visitor, reloadingStrategy);
    }
}