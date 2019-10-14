package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.Visitors.ForClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
class ClassOperatorBaseTest extends AbstractOperatorTest<ClassIdentifier, ForClass> {
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
    protected OperatorBase<ClassIdentifier, ForClass> buildSut() {
        return new DummyClassOperatorBase(buddy, reloadingStrategy);
    }

    private static class DummyClassOperatorBase extends ClassOperatorBase {

        /**
         * For test purpose
         *
         * @param byteBuddy
         * @param classLoadingStrategy
         */
        DummyClassOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected ForClass visitor(ClassIdentifier targetIdentifier) {
            return null;
        }
    }
}