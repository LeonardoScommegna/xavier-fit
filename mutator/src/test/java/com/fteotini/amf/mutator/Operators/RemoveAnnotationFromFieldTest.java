package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
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
            sut.runMutation(MutationDetails.ForField(fieldName, "com.fteotini.amf.mutator.Operators.RemoveAnnotationFromFieldTest$Dummy"));

            var obj = Dummy.class;

            assertThat(obj.isAnnotationPresent(DummyAnnotation.class)).isTrue();

            assertThat(obj.getDeclaredMethod("run").isAnnotationPresent(DummyAnnotation.class)).isTrue();
            assertThat(obj.getDeclaredField("bar").isAnnotationPresent(DummyAnnotation.class)).isTrue();

            var fooField = obj.getDeclaredField(fieldName);
            assertThat(fooField.isAnnotationPresent(DummyAnnotation.class)).isFalse();
            assertThat(fooField.isAnnotationPresent(DummyAnnotation2.class)).isTrue();
        }
    }

    /*@Test
    void second() throws IOException, NoSuchFieldException {
        Map<String,Object> map = Map.ofEntries(
                Map.entry("value","pluto"),
                Map.entry("strings",new String[]{"3"}),
                Map.entry("en",DummyEnum.last)
        );

        var annotationVisitorWrapper = new AnnotationValuesReplacer<>(DummyAnnotation.class, map);
        try(var sut = new FieldOperator(new ByteBuddy(),fieldIdentifier -> new ForField(annotationVisitorWrapper,named(fieldIdentifier.getName())))) {
            sut.runMutation(MutationDetails.ForField("foo", "com.fteotini.amf.mutator.Operators.Dummy"));

            var fieldAnn = Dummy.class.getDeclaredField("foo").getAnnotation(DummyAnnotation.class);
            assertThat(fieldAnn.value()).isEqualTo("pluto");
            assertThat(fieldAnn.en()).isEqualTo(DummyEnum.last);
            assertThat(fieldAnn.strings()).containsOnly("3");
        }
    }*/

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

/*class Dummy2 {
    @DummyAnnotation(strings = {"1","2"},ann = @DummyAnnotation2, en = DummyEnum.first)
    private String foo;
}*/



/*@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@interface DummyAnnotation {
    String value() default "pippo";
    String[] strings();

    DummyEnum en();
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@interface DummyAnnotation2 {
    int value() default 1;
}

enum DummyEnum {
    first,
    last
}*/
