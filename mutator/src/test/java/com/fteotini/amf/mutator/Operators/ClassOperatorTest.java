package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class ClassOperatorTest extends AbstractOperatorTest<ClassIdentifier> {
    @Test
    void Given_a_mutationDetails_it_will_select_the_classIdentifier_and_return_its_name() {
        var clazz = getClass();
        var className = clazz.getCanonicalName();
        var identifier = new ClassIdentifier(className);
        when(mutationDetails.getClassIdentifier()).thenReturn(Optional.of(identifier));

        sut.runMutation(mutationDetails);

        verify(buddy).decorate(clazz);
    }

    @Override
    protected OperatorBase<ClassIdentifier> buildSut() {
        return new ClassOperator(buddy, classIdentifier -> visitor, reloadingStrategy);
    }
}