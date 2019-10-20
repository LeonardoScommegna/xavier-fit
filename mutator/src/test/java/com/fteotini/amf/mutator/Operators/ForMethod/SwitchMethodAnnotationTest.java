package com.fteotini.amf.mutator.Operators.ForMethod;

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
class SwitchMethodAnnotationTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_switch_a__method_annotation_with_another() throws IOException, NoSuchMethodException {
        try (var sut = new SwitchMethodAnnotation<>(new ByteBuddy(), From.class, To.class)) {
            sut.runMutation(MutationTarget.ForMethod("foo", new String[0], "com.fteotini.amf.mutator.Operators.ForMethod.SwitchMethodAnnotationTest$Dummy"));

            var method = Dummy.class.getDeclaredMethod("foo");
            assertThat(method.isAnnotationPresent(From.class)).isFalse();
            assertThat(method.isAnnotationPresent(To.class)).isTrue();
        }
    }

    //<editor-fold desc="Dummy classes">
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface From {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface To {
    }

    private static class Dummy {
        @From
        private String foo() {
            return null;
        }
    }
    //</editor-fold>
}