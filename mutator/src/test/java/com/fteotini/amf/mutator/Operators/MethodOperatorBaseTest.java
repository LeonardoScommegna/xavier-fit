package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.amf.mutator.Visitors.ForMethod;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class MethodOperatorBaseTest extends AbstractOperatorTest<MethodIdentifier, ForMethod> {
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
    protected OperatorBase<MethodIdentifier, ForMethod> buildSut() {
        return new DummyMethodOperatorBase(buddy, reloadingStrategy);
    }

    private static class DummyMethodOperatorBase extends MethodOperatorBase {
        /**
         * For test purpose
         *
         * @param byteBuddy
         * @param classLoadingStrategy
         */
        DummyMethodOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected ForMethod visitor(MethodIdentifier targetIdentifier) {
            return null;
        }
    }
}