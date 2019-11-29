package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.IMutationTarget;
import com.fteotini.Xavier.mutator.MutationTarget;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;

import java.util.Arrays;
import java.util.stream.Stream;

public class MethodOperatorTargetsFinder extends OperatorTargetsFinderBase<MethodInfo> {
    private static String[] getMethodParameterTypes(MethodInfo methodInfo) {
        return Arrays.stream(methodInfo.getParameterInfo())
                .map(x -> x.getTypeDescriptor().toString())
                .toArray(String[]::new);
    }

    @Override
    protected final IMutationTarget getMutationDetails(MethodInfo entityInfo) {
        return MutationTarget.ForMethod(entityInfo.getName(), getMethodParameterTypes(entityInfo), entityInfo.getClassInfo().getName());
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
