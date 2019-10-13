package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.MutationDetails;
import com.fteotini.amf.mutator.MutationDetailsInterface;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.FieldInfo;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

abstract class FieldOperatorTargetsFinderBase<A extends Annotation> extends OperatorTargetsFinderBase<A, FieldInfo> {
    @Override
    protected final MutationDetailsInterface getMutationDetails(FieldInfo entityInfo) {
        return MutationDetails.ForField(entityInfo.getName(), entityInfo.getClassInfo().getName());
    }

    @Override
    protected final boolean initialClassFilter(ClassInfo classInfo) {
        return classInfo.hasFieldAnnotation(targetAnnotationName());
    }

    @Override
    protected final Stream<FieldInfo> toResultTypeStream(ClassInfoList classInfoList) {
        return classInfoList.stream()
                .flatMap(classInfo -> classInfo.getFieldInfo().stream())
                .filter(fieldInfo -> fieldInfo.hasAnnotation(targetAnnotationName()));
    }
}
