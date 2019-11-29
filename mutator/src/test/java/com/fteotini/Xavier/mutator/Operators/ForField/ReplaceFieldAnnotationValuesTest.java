package com.fteotini.Xavier.mutator.Operators.ForField;

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
class ReplaceFieldAnnotationValuesTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_replace_the_annotation_values_of_a_field() throws IOException, NoSuchFieldException {
        Map<String, Object> map = Map.ofEntries(
                Map.entry("value", "pluto"),
                Map.entry("strings", new String[]{"3"}),
                Map.entry("en", DummyEnum.last)
        );

        try (var sut = new ReplaceFieldAnnotationValues<>(new ByteBuddy(), DummyAnnotation.class, map)) {
            var fieldName = "foo";
            sut.runMutation(MutationTarget.ForField(fieldName, "com.fteotini.Xavier.mutator.Operators.ForField.ReplaceFieldAnnotationValuesTest$Dummy"));

            var fieldAnn = Dummy.class.getDeclaredField(fieldName).getAnnotation(DummyAnnotation.class);
            assertThat(fieldAnn.value()).isEqualTo("pluto");
            assertThat(fieldAnn.en()).isEqualTo(DummyEnum.last);
            assertThat(fieldAnn.strings()).containsOnly("3");
        }
    }

    //<editor-fold desc="Dummy classes">
    private enum DummyEnum {
        first,
        last
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    private @interface DummyAnnotation {
        String value() default "pippo";

        String[] strings();

        DummyEnum en();
    }

    private static class Dummy {
        @DummyAnnotation(strings = {"1", "2"}, en = DummyEnum.first)
        private String foo;
    }
    //</editor-fold>
}