package com.fteotini.Xavier.mutator.Operators.Base;

import com.fteotini.Xavier.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.Xavier.mutator.MutationIdentifiers.MethodIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
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
        return new DummyMethodOperator(buddy, reloadingStrategy);
    }

    private class DummyMethodOperator extends MethodOperator {

        /**
         * For test purpose
         *
         * @param byteBuddy
         * @param classLoadingStrategy
         */
        DummyMethodOperator(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected AsmVisitorWrapper visitor(MethodIdentifier identifier) {
            return visitor;
        }
    }
}