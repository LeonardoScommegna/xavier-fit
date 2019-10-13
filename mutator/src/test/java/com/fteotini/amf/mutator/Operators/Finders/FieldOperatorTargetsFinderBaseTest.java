package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.OperatorTarget;
import com.fteotini.amf.mutator.Operators.Finders.operatorTestEntities.DummyAnnotation;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class FieldOperatorTargetsFinderBaseTest extends BaseIntegrationTestForOperator {
    @SuppressWarnings("unchecked")
    @Test
    void Given_an_operator_set_to_work_on_fields_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new FieldOperatorTargetsFinder();

        var result = sut.findMutations(scanResult);

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

class FieldOperatorTargetsFinder extends FieldOperatorTargetsFinderBase<DummyAnnotation> {
    @Override
    protected Class<DummyAnnotation> targetAnnotation() {
        return DummyAnnotation.class;
    }
}