package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.operatorTestEntities.DummyAnnotation;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Tag("IntegrationTest")
class OperatorBaseTest {
    private static final String PKG_ID = "com.fteotini.amf.mutator.operatorTestEntities";
    private static ScanResult scanResult;

    @BeforeAll
    static void setUp() {
        scanResult = new ClassGraph()
                //.verbose()
                .enableAllInfo()
                .whitelistPackages(PKG_ID)
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
                .extracting(x -> x.getClassIdentifier().get().getFullName())
                .containsExactlyInAnyOrder(getClassFullName("DummyClass1"), getClassFullName("DummyClass2"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void Given_an_operator_set_to_work_on_methods_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new MethodOperator();

        var result = sut.findMutations(scanResult);

        assertThat(result).hasSize(3)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Method))
                .extracting(x -> x.getMethodIdentifier().get())
                .extracting(MethodIdentifier::getMethodSimpleName, MethodIdentifier::getParametersType, methodIdentifier -> methodIdentifier.getBelongingClass().getFullName())
                .containsExactlyInAnyOrder(
                        new Tuple("annotated_method", new String[]{"java.lang.String"}, getClassFullName("DummyClass2")),
                        new Tuple("another_annotated_method", new String[0], getClassFullName("DummyClass2")),
                        new Tuple("annotated_method", new String[]{"int"}, getClassFullName("DummyClass3"))
                );
    }

    @SuppressWarnings("unchecked")
    @Test
    void Given_an_operator_set_to_work_on_fields_then_it_should_build_a_set_of_mutation_details_containing_all_the_relevant_data() {
        var sut = new FieldOperator();

        var result = sut.findMutations(scanResult);

        assertThat(result).hasSize(3)
                .allSatisfy(x -> assertThat(x.getTargetElementType()).isEqualTo(OperatorTarget.Field))
                .extracting(x -> x.getFieldIdentifier().get())
                .extracting(FieldIdentifier::getFieldSimpleName, methodIdentifier -> methodIdentifier.getBelongingClass().getFullName())
                .containsExactlyInAnyOrder(
                        new Tuple("field1", getClassFullName("DummyClass1")),
                        new Tuple("field1", getClassFullName("DummyClass3")),
                        new Tuple("field2", getClassFullName("DummyClass3"))
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

class FieldOperator extends OperatorBase {

    @Override
    protected Class<? extends Annotation> targetAnnotation() {
        return DummyAnnotation.class;
    }

    @Override
    protected OperatorTarget operatorTarget() {
        return OperatorTarget.Field;
    }
}
//endregion
