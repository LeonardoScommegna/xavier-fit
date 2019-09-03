package com.fteotini.amf.tester.dummyProject;

import com.fteotini.amf.tester.Subject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubjectTest {
    private final Subject sut;

    public SubjectTest() {
        sut = new Subject();
    }

    @Test
    void it_adds_5() {
        var x = 3;

        var result = sut.addFive(x);

        assertEquals(8,result);
    }
}
