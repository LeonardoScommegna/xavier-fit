package com.fteotini.amf.mutator.Operators.ForClass;

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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class ReplaceClassAnnotationValuesTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_change_a_class_annotation_value() throws IOException {
        Map<String, Object> newValues = Map.ofEntries(Map.entry("value", "Bar"));
        try (var sut = new ReplaceClassAnnotationValues<>(new ByteBuddy(), DummyAnnotation.class, newValues)) {
            sut.runMutation(MutationTarget.ForClass("com.fteotini.amf.mutator.Operators.ForClass.ReplaceClassAnnotationValuesTest$Dummy"));

            assertThat(Dummy.class.getAnnotation(DummyAnnotation.class).value()).isEqualTo("Bar");
        }
    }

    //<editor-fold desc="Dummy classes">
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    private @interface DummyAnnotation {
        String value();
    }

    @DummyAnnotation("Foo")
    private static class Dummy {
    }
    //</editor-fold>
}