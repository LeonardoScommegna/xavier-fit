package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.Visitors.ForField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class FieldOperatorBaseTest extends AbstractOperatorTest<FieldIdentifier, ForField> {

    @Test
    void Given_a_mutationDetails_it_will_select_the_fieldIdentifier_and_return_the_belonging_class_name() {
        var clazz = getClass();
        var className = clazz.getCanonicalName();
        var identifier = new FieldIdentifier("foo", new ClassIdentifier(className));
        when(mutationDetails.getFieldIdentifier()).thenReturn(Optional.of(identifier));

        sut.runMutation(mutationDetails);

        verify(buddy).decorate(clazz);
    }

    @Override
    protected OperatorBase<FieldIdentifier, ForField> buildSut() {
        return new DummyFieldOperatorBase(buddy, reloadingStrategy);
    }

    private static class DummyFieldOperatorBase extends FieldOperatorBase {
        /**
         * For test purpose
         *
         * @param byteBuddy
         * @param classLoadingStrategy
         */
        DummyFieldOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected ForField visitor(FieldIdentifier targetIdentifier) {
            return null;
        }
    }
}