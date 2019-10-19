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
class SwitchFieldAnnotationTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_switch_field_annotations() throws IOException, NoSuchFieldException {
        try (var sut = new SwitchFieldAnnotation<>(new ByteBuddy(), From.class, To.class)) {
            sut.runMutation(MutationTarget.ForField("foo", "com.fteotini.amf.mutator.Operators.SwitchFieldAnnotationTest$Dummy"));

            var field = Dummy.class.getDeclaredField("foo");

            assertThat(field.isAnnotationPresent(From.class)).isFalse();
            assertThat(field.isAnnotationPresent(To.class)).isTrue();
            assertThat(field.getAnnotation(To.class).value()).isEqualTo(3);
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

    private static class Dummy {
        @From
        String foo;
    }
    //</editor-fold>
}