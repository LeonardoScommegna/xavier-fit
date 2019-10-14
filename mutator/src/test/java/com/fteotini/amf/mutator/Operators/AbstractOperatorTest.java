package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import com.fteotini.amf.mutator.MutationIdentifiers.Identifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Answers;
import org.mockito.Mock;

import static org.mockito.Mockito.lenient;

abstract class AbstractOperatorTest<T extends Identifier, K extends AsmVisitorWrapper> {
    @Mock
    protected MutationDetailsInterface mutationDetails;
    @Mock(answer = Answers.RETURNS_SELF)
    protected DynamicType.Builder<OperatorBaseTest> buddyBuilder;
    @Mock
    protected DynamicType.Unloaded<OperatorBaseTest> buddyUnloaded;
    @Mock
    protected ByteBuddy buddy;
    @Mock
    protected ClassReloadingStrategy reloadingStrategy;

    protected OperatorBase<T, K> sut;

    @BeforeEach
    void setUp() {
        lenient().when(buddy.decorate(getClass())).thenAnswer(invocation -> buddyBuilder);
        lenient().when(buddyBuilder.make()).thenReturn(buddyUnloaded);

        sut = buildSut();
    }

    protected abstract OperatorBase<T, K> buildSut();
}
