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
class SwitchClassAnnotationTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_switch_class_annotations() throws IOException {
        try (var sut = new SwitchClassAnnotation<>(new ByteBuddy(), From.class, To.class)) {
            sut.runMutation(MutationTarget.ForClass("com.fteotini.amf.mutator.Operators.SwitchClassAnnotationTest$Dummy"));

            var clazz = Dummy.class;

            assertThat(clazz.isAnnotationPresent(From.class)).isFalse();
            assertThat(clazz.isAnnotationPresent(To.class)).isTrue();
            assertThat(clazz.getAnnotation(To.class).value()).isEqualTo(3);
        }
    }

    //<editor-fold desc="Dummy classes">
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
    private @interface From {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
    private @interface To {
        int value() default 3;
    }

    @From
    private static class Dummy {
    }
    //</editor-fold>
}