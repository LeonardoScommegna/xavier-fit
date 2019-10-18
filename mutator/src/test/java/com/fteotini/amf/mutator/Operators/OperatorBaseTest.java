package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
class OperatorBaseTest extends AbstractOperatorTest<ClassIdentifier> {
    private String className = getClass().getCanonicalName();

    @Test
    void Given_an_empty_optional_returned_from_getMutationTarget_then_it_should_not_do_anything() {
        sut.runMutation(mock(MutationDetailsInterface.class));

        verifyZeroInteractions(buddy, reloadingStrategy);
    }

    @Test
    void When_closing_then_it_should_reset_the_reloading_strategy() throws IOException {
        sut.close();
        verify(reloadingStrategy).reset(any());
    }

    @Test
    void Given_an_identifier_containing_a_non_existing_className_then_it_should_throw_an_unchecked_exception() {
        className = "ASdasdasdasdasd";

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sut.runMutation(mutationDetails))
                .withCauseInstanceOf(ClassNotFoundException.class);
    }

    @Test
    void Given_an_identifier_containing_a_existing_className_then_it_should_decorate_it_with_byteBuddy() {
        sut.runMutation(mutationDetails);

        verify(buddy).decorate(getClass());
    }

    @Test
    void Given_a_decorated_class_then_it_should_chain_the_correct_visitor_make_the_mutant_and_load_it() {
        sut.runMutation(mutationDetails);

        var inOrder = inOrder(buddyBuilder, buddyUnloaded);
        inOrder.verify(buddyBuilder).visit(visitor);
        inOrder.verify(buddyBuilder).make();
        inOrder.verify(buddyUnloaded).load(getClass().getClassLoader(), reloadingStrategy);
    }

    @BeforeEach
    @Override
    void setUp() {
        lenient().when(mutationDetails.getClassIdentifier()).thenAnswer(invocation -> Optional.of(new ClassIdentifier(className)));
        super.setUp();
    }

    @Override
    protected OperatorBase<ClassIdentifier> buildSut() {
        return new DummyOperatorBase(buddy, reloadingStrategy);
    }

    //<editor-fold desc="Dummy Classes">
    private class DummyOperatorBase extends OperatorBase<ClassIdentifier> {

        DummyOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected AsmVisitorWrapper visitor(ClassIdentifier identifier) {
            return visitor;
        }

        @Override
        protected String className(ClassIdentifier identifier) {
            return identifier.getName();
        }

        @Override
        protected Optional<ClassIdentifier> getMutationTarget(MutationDetailsInterface mutationDetailsInterface) {
            return mutationDetailsInterface.getClassIdentifier();
        }

    }
    //</editor-fold>
}

