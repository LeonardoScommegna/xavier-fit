package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationTarget;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class RemoveAnnotationFromFieldTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_remove_an_annotation_from_a_single_field() throws NoSuchMethodException, NoSuchFieldException, IOException {
        try (var sut = new RemoveAnnotationFromField<>(new ByteBuddy(), DummyAnnotation.class)) {
            var fieldName = "foo";
            sut.runMutation(MutationTarget.ForField(fieldName, "com.fteotini.amf.mutator.Operators.RemoveAnnotationFromFieldTest$Dummy"));

            var obj = Dummy.class;

            assertThat(obj.isAnnotationPresent(DummyAnnotation.class)).isTrue();

            assertThat(obj.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation.class)).isTrue();
            assertThat(obj.getDeclaredField("bar").isAnnotationPresent(DummyAnnotation.class)).isTrue();

            var fooField = obj.getDeclaredField(fieldName);
            assertThat(fooField.isAnnotationPresent(DummyAnnotation.class)).isFalse();
            assertThat(fooField.isAnnotationPresent(DummyAnnotation2.class)).isTrue();
        }
    }

    //<editor-fold desc="DummyClasses">
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @interface DummyAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @interface DummyAnnotation2 {
    }

    @DummyAnnotation
    static class Dummy {
        @DummyAnnotation
        @DummyAnnotation2
        private String foo;

        @DummyAnnotation
        private String bar;

        @DummyAnnotation
        void run() {
        }
    }
    //</editor-fold>
}