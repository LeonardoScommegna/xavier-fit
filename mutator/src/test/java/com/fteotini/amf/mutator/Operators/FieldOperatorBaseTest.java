/*
package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.Visitors.ForField;
import com.fteotini.amf.mutator.Visitors.RemoveAnnotation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.FieldVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@interface DummyAnnotation {
}

@Tag("IntegrationTest")
class FieldOperatorBaseTest {
    @BeforeEach
    void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void name() throws NoSuchMethodException, NoSuchFieldException, IOException {

        try (var sut = new FieldOp(new ByteBuddy())) {
            sut.runMutation(MutationDetails.ForField("foo", "com.fteotini.amf.mutator.Operators.Dummy"));

            var obj = Dummy.class;
            assertThat(obj.isAnnotationPresent(DummyAnnotation.class)).isTrue();
            assertThat(obj.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation.class)).isTrue();
            assertThat(obj.getDeclaredField("foo").isAnnotationPresent(DummyAnnotation.class)).isFalse();
        }
    }

    @Test
    void name2() throws NoSuchMethodException, NoSuchFieldException {
        var obj = Dummy.class;
        assertThat(obj.isAnnotationPresent(DummyAnnotation.class)).isTrue();
        assertThat(obj.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation.class)).isTrue();
        assertThat(obj.getDeclaredField("foo").isAnnotationPresent(DummyAnnotation.class)).isTrue();
    }
}

class FieldOp extends FieldOperatorBase {
    public FieldOp(ByteBuddy byteBuddy) {
        super(byteBuddy);
    }

    @Override
    protected AsmVisitorWrapper visitor(FieldIdentifier targetIdentifier) {
        return new ForField(new RemoveAnnotation<>(DummyAnnotation.class));
    }
}

@DummyAnnotation
class Dummy {
    @DummyAnnotation
    private String foo;

    @DummyAnnotation
    void run() {
    }
}*/
