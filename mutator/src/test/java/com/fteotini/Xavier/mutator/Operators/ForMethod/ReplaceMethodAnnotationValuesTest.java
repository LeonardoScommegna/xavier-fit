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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class ReplaceMethodAnnotationValuesTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_change_a_field_annotation_value() throws IOException, NoSuchMethodException {
        Map<String, Object> newValues = Map.ofEntries(Map.entry("flags", new int[]{2, 3}));

        try (var sut = new ReplaceMethodAnnotationValues<>(new ByteBuddy(), DummyAnnotation.class, newValues)) {
            sut.runMutation(MutationTarget.ForMethod("run", new String[0], "com.fteotini.Xavier.mutator.Operators.ForMethod.ReplaceMethodAnnotationValuesTest$Dummy"));

            var method = Dummy.class.getDeclaredMethod("run");
            assertThat(method.getAnnotation(DummyAnnotation.class).flags()).containsExactly(2, 3);
        }
    }

    //<editor-fold desc="Dummy classes">
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface DummyAnnotation {
        int[] flags();
    }

    private static class Dummy {
        @DummyAnnotation(flags = 2)
        private void run() {
        }
    }
    //</editor-fold>
}