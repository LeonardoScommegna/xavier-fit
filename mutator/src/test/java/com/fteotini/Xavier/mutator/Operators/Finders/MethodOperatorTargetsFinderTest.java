package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.MutationIdentifiers.MethodIdentifier;
import com.fteotini.Xavier.mutator.OperatorTarget;
import com.fteotini.Xavier.mutator.Operators.operatorTestEntities.DummyAnnotation;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class MethodOperatorTargetsFinderTest extends BaseIntegrationTestForOperator {
    @SuppressWarnings("unchecked")
    @Test
    void Given_an_operator_set_to_work_on_methods_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new MethodOperatorTargetsFinder();

        var result = sut.findTargets(scanResult, DummyAnnotation.class);

        assertThat(result).hasSize(3)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Method))
                .extracting(x -> x.getMethodIdentifier().get())
                .extracting(MethodIdentifier::getName, MethodIdentifier::getParametersType, methodIdentifier -> methodIdentifier.getBelongingClass().getName())
                .containsExactlyInAnyOrder(
                        new Tuple("annotated_method", new String[]{"java.lang.String"}, getClassFullName("DummyClass2")),
                        new Tuple("another_annotated_method", new String[0], getClassFullName("DummyClass2")),
                        new Tuple("annotated_method", new String[]{"int"}, getClassFullName("DummyClass3"))
                );
    }
}