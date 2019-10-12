package com.fteotini.amf.mutator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class MutationDetailsTest {

    @Test
    void When_constructed_for_class_then_it_should_fill_only_the_relevant_properties() {
        final var classFullName = "com.ft.Dummy";
        var sut = MutationDetails.ForClass(classFullName);

        assertThat(sut.getTargetElementType()).isEqualTo(OperatorTarget.Class);
        assertThat(sut.getClassIdentifier()).isNotEmpty().get()
                .extracting(ClassIdentifier::getFullName).isEqualTo(classFullName);

        assertThat(sut.getFieldIdentifier()).isEmpty();
        assertThat(sut.getMethodIdentifier()).isEmpty();
    }

    @Test
    void When_constructed_for_method_then_it_should_fill_only_the_relevant_properties() {
        final var methodSimpleName = "method";
        final var methodParamTypes = new String[]{"java.lang.String"};
        final var classFullName = "com.ft.Dummy";

        var sut = MutationDetails.ForMethod(methodSimpleName, methodParamTypes, classFullName);

        assertThat(sut.getTargetElementType()).isEqualTo(OperatorTarget.Method);
        //noinspection unchecked
        assertThat(sut.getMethodIdentifier()).isNotEmpty().get()
                .extracting(
                        MethodIdentifier::getMethodSimpleName,
                        MethodIdentifier::getParametersType,
                        x -> x.getBelongingClass().getFullName()
                )
                .containsExactly(methodSimpleName, methodParamTypes, classFullName);


        assertThat(sut.getFieldIdentifier()).isEmpty();
        assertThat(sut.getClassIdentifier()).isEmpty();
    }

    @Test
    void When_constructed_for_field_then_it_should_fill_only_the_relevant_properties() {
        final var fieldName = "field";
        final var classFullName = "com.ft.Dummy";

        var sut = MutationDetails.ForField(fieldName, classFullName);

        assertThat(sut.getTargetElementType()).isEqualTo(OperatorTarget.Field);
        //noinspection unchecked
        assertThat(sut.getFieldIdentifier()).isNotEmpty().get()
                .extracting(FieldIdentifier::getFieldSimpleName, x -> x.getBelongingClass().getFullName())
                .containsExactly(fieldName, classFullName);


        assertThat(sut.getMethodIdentifier()).isEmpty();
        assertThat(sut.getClassIdentifier()).isEmpty();
    }
}