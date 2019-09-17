package com.fteotini.amf.launcher.minion;

import com.fteotini.amf.launcher.minion.MethodUnderTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

@Tag("UnitTest")
class MethodUnderTestUnitTest {

    @Test
    void It_should_be_serializable() throws NoSuchMethodException {
        var sut = new MethodUnderTest(Dummy.class, Dummy.class.getMethod("hello"));

        assertThat(sut).isInstanceOf(Serializable.class);
    }

    @Test
    void Given_a_Method_not_belonging_to_the_given_class_then_it_should_throw() {
        ThrowingCallable act = () -> new MethodUnderTest(Dummy.class, Dummy2.class.getMethod("hello"));

        assertThatIllegalArgumentException().isThrownBy(act)
                .withMessage("The provided method does not belong to the provided class");
    }

    static class Dummy {
        public void hello() {
        }
    }

    static class Dummy2 {
        public void hello() {
        }
    }
}