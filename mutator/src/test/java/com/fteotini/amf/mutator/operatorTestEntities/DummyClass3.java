package com.fteotini.amf.mutator.operatorTestEntities;

public class DummyClass3 {
    @DummyAnnotation
    public double field2;
    @DummyAnnotation
    DummyClass2 field1;

    @DummyAnnotation
    public void annotated_method(int value) {
    }

    public void not_annotated_method() {
    }
}
