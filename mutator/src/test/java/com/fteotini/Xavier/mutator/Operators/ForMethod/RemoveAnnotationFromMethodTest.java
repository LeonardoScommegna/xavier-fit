package com.fteotini.Xavier.mutator.Operators.ForMethod;

import com.fteotini.Xavier.mutator.MutationTarget;
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
class RemoveAnnotationFromMethodTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_remove_an_annotation_from_method() throws IOException, NoSuchMethodException {
        try (var sut = new RemoveAnnotationFromMethod<>(new ByteBuddy(), DummyAnnotation.class)) {
            sut.runMutation(MutationTarget.ForMethod("run", new String[0], "com.fteotini.Xavier.mutator.Operators.ForMethod.RemoveAnnotationFromMethodTest$Dummy"));

            var clazz = Dummy.class;
            assertThat(clazz.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation.class)).isFalse();
            assertThat(clazz.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation2.class)).isTrue();
            assertThat(clazz.getDeclaredMethod("run", int.class).isAnnotationPresent(DummyAnnotation.class)).isTrue();
        }
    }

    //<editor-fold desc="DummyClasses">
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface DummyAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface DummyAnnotation2 {
    }

    private static class Dummy {
        @DummyAnnotation
        @DummyAnnotation2
        void run() {
        }

        @DummyAnnotation
        void run(int num) {
        }
    }
    //</editor-fold>
}