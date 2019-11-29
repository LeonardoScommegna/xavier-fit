package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.OperatorTarget;
import com.fteotini.Xavier.mutator.Operators.operatorTestEntities.DummyAnnotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassOperatorTargetsFinderTest extends BaseIntegrationTestForOperator {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void Given_an_operator_set_to_work_on_classes_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new ClassOperatorTargetsFinder();

        var result = sut.findTargets(scanResult, DummyAnnotation.class);

        assertThat(result).hasSize(2)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Class))
                .extracting(x -> x.getClassIdentifier().get().getName())
                .containsExactlyInAnyOrder(getClassFullName("DummyClass1"), getClassFullName("DummyClass2"));
    }
}