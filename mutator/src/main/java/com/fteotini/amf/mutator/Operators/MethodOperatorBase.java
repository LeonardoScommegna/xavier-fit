package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

abstract class MethodOperatorBase<A extends Annotation> extends OperatorBase<A, MethodInfo> {
    private static String[] getMethodParameterTypes(MethodInfo methodInfo) {
        return Arrays.stream(methodInfo.getParameterInfo())
                .map(x -> x.getTypeDescriptor().toString())
                .toArray(String[]::new);
    }

    @Override
    protected final MutationDetails getMutationDetails(MethodInfo entityInfo) {
        return MutationDetails.ForMethod(entityInfo.getName(), getMethodParameterTypes(entityInfo), entityInfo.getClassInfo().getName());
    }

    @Override
    protected final boolean initialClassFilter(ClassInfo classInfo) {
        return classInfo.hasMethodAnnotation(targetAnnotationName());
    }

    @Override
    protected final Stream<MethodInfo> toResultTypeStream(ClassInfoList classInfoList) {
        return classInfoList.stream()
                .flatMap(classInfo -> classInfo.getMethodAndConstructorInfo().stream())
                .filter(methodInfo -> methodInfo.hasAnnotation(targetAnnotationName()));
    }
}
