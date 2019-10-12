package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.FieldInfo;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

abstract class FieldOperatorBase<A extends Annotation> extends OperatorBase<A, FieldInfo> {
    @Override
    protected final MutationDetails getMutationDetails(FieldInfo entityInfo) {
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
