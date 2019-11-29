package com.fteotini.Xavier.mutator.Operators.ForClass;

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
class RemoveAnnotationFromClassTest {
    @BeforeAll
    static void setUp() {
        ByteBuddyAgent.install();
    }

    @Test
    void it_can_remove_a_given_annotation_from_class() throws IOException {
        try (var sut = new RemoveAnnotationFromClass<>(new ByteBuddy(), ToBeRemoved.class)) {
            sut.runMutation(MutationTarget.ForClass("com.fteotini.Xavier.mutator.Operators.ForClass.RemoveAnnotationFromClassTest$Dummy"));

            assertThat(Dummy.class.isAnnotationPresent(ToBeRemoved.class)).isFalse();
        }
    }

    //<editor-fold desc="Dummy classes">
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
    private @interface ToBeRemoved {
    }

    @ToBeRemoved
    private static class Dummy {
    }
    //</editor-fold>
}