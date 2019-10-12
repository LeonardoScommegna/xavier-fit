package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.OperatorTarget;
import com.fteotini.amf.mutator.Operators.operatorTestEntities.DummyAnnotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassOperatorBaseTest extends BaseIntegrationTestForOperator {
    @Test
    void Given_an_operator_set_to_work_on_classes_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new ClassOperator();

        var result = sut.findMutations(scanResult);

        assertThat(result).hasSize(2)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Class))
                .extracting(x -> x.getClassIdentifier().get().getFullName())
                .containsExactlyInAnyOrder(getClassFullName("DummyClass1"), getClassFullName("DummyClass2"));
    }
}

class ClassOperator extends ClassOperatorBase<DummyAnnotation> {
    @Override
    protected Class<DummyAnnotation> targetAnnotation() {
        return DummyAnnotation.class;
    }
}