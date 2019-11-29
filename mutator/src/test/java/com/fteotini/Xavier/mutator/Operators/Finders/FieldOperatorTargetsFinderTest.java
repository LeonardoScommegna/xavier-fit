package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.Xavier.mutator.OperatorTarget;
import com.fteotini.Xavier.mutator.Operators.operatorTestEntities.DummyAnnotation;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class FieldOperatorTargetsFinderTest extends BaseIntegrationTestForOperator {
    @SuppressWarnings("unchecked")
    @Test
    void Given_an_operator_set_to_work_on_fields_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new FieldOperatorTargetsFinder();

        var result = sut.findTargets(scanResult, DummyAnnotation.class);

        assertThat(result).hasSize(3)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Field))
                .extracting(x -> x.getFieldIdentifier().get())
                .extracting(FieldIdentifier::getName, methodIdentifier -> methodIdentifier.getBelongingClass().getName())
                .containsExactlyInAnyOrder(
                        new Tuple("field1", getClassFullName("DummyClass1")),
                        new Tuple("field1", getClassFullName("DummyClass3")),
                        new Tuple("field2", getClassFullName("DummyClass3"))
                );
    }
}