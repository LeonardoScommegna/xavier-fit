package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.Operators.OperatorBaseTest.DummyAsm;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class OperatorBaseTest extends AbstractOperatorTest<ClassIdentifier, DummyAsm> {
    private DummyAsm dummyAsm = new DummyAsm();

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
        inOrder.verify(buddyBuilder).visit(dummyAsm);
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
    protected OperatorBase<ClassIdentifier, DummyAsm> buildSut() {
        return new DummyOperatorBase(buddy, reloadingStrategy);
    }

    //<editor-fold desc="Dummy Classes">
    static class DummyAsm implements AsmVisitorWrapper {
        @Override
        public int mergeWriter(int flags) {
            return 0;
        }

        @Override
        public int mergeReader(int flags) {
            return 0;
        }

        @Override
        public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
            return null;
        }
    }
    private class DummyOperatorBase extends OperatorBase<ClassIdentifier, DummyAsm> {
        DummyOperatorBase(ByteBuddy byteBuddy, ClassReloadingStrategy classLoadingStrategy) {
            super(byteBuddy, classLoadingStrategy);
        }

        @Override
        protected String className(ClassIdentifier identifier) {
            return identifier.getName();
        }

        @Override
        protected Optional<ClassIdentifier> getMutationTarget(MutationDetailsInterface mutationDetailsInterface) {
            return mutationDetailsInterface.getClassIdentifier();
        }

        @Override
        protected OperatorBaseTest.DummyAsm visitor(ClassIdentifier targetIdentifier) {
            return dummyAsm;
        }
    }
    //</editor-fold>
}

