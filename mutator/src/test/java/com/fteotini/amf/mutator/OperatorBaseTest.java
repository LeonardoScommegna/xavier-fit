package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.operatorTestEntities.DummyAnnotation;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class OperatorBaseTest {
    private static final String PKG_ID = "com.fteotini.amf.mutator.operatorTestEntities";
    private static ScanResult scanResult;

    @BeforeAll
    static void setUp() {
        scanResult = new ClassGraph()
                //.verbose()
                .enableAllInfo().whitelistPackages(PKG_ID)
                .scan();
    }

    @AfterAll
    static void tearDown() {
        scanResult.close();
    }

    @Test
    void Given_an_operator_set_to_work_on_classes_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new ClassOperator();

        var result = sut.findMutations(scanResult);

        assertThat(result).hasSize(2)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Class))
                .extracting(MutationDetails::getAnnotationTargetIdentifier)
                .containsExactlyInAnyOrder(getClassFullName("DummyClass1"), getClassFullName("DummyClass2"));
    }

    @Test
    void Given_an_operator_set_to_work_on_methods_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new MethodOperator();

        var result = sut.findMutations(scanResult);

        assertThat(result).hasSize(3)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Method))
                .extracting(MutationDetails::getAnnotationTargetIdentifier)
                .containsExactlyInAnyOrder(
                        getMethodFullName("DummyClass2", "annotated_method"),
                        getMethodFullName("DummyClass2", "another_annotated_method"),
                        getMethodFullName("DummyClass3", "annotated_method")
                );
    }

    private static String getClassFullName(String className) {
        return PKG_ID + "." + className;
    }

    private static String getMethodFullName(String className, String methodName) {
        return getClassFullName(className) + "." + methodName;
    }
}

//region TestOperators
class ClassOperator extends OperatorBase {
    @Override
    protected Class<? extends Annotation> targetAnnotation() {
        return DummyAnnotation.class;
    }

    @Override
    protected OperatorTarget operatorTarget() {
        return OperatorTarget.Class;
    }
}

class MethodOperator extends OperatorBase {

    @Override
    protected Class<? extends Annotation> targetAnnotation() {
        return DummyAnnotation.class;
    }

    @Override
    protected OperatorTarget operatorTarget() {
        return OperatorTarget.Method;
    }
}
//endregion
