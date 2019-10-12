package com.fteotini.amf.mutator;

import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

abstract class OperatorBase implements Operator {
    @Override
    public Set<MutationDetails> findMutations(ScanResult scanResult) {
        var classes = scanResult.getAllStandardClasses();

        var operatorTarget = operatorTarget();
        switch (operatorTarget) {
            case Class:
                return classes
                        .filter(classInfo -> classInfo.hasAnnotation(targetAnnotationName()))
                        .getNames()
                        .stream()
                        .map(className -> new MutationDetails(className, operatorTarget))
                        .collect(Collectors.toUnmodifiableSet());
            case Method:
                return classes
                        .filter(classInfo -> classInfo.hasMethodAnnotation(targetAnnotationName()))
                        .stream()
                        .flatMap(classInfo -> classInfo.getMethodAndConstructorInfo().stream())
                        .filter(methodInfo -> methodInfo.hasAnnotation(targetAnnotationName()))
                        .map(methodInfo -> new MutationDetails(methodInfo.toString(), operatorTarget))
                        .collect(Collectors.toUnmodifiableSet());
            case Field:
                return classes
                        .filter(classInfo -> classInfo.hasFieldAnnotation(targetAnnotationName()))
                        .stream()
                        .flatMap(classInfo -> classInfo.getFieldInfo().stream())
                        .filter(fieldInfo -> fieldInfo.hasAnnotation(targetAnnotationName()))
                        .map(fieldInfo -> new MutationDetails(fieldInfo.getName(), operatorTarget))
                        .collect(Collectors.toUnmodifiableSet());
            default:
                throw new IllegalStateException("Unexpected value: " + operatorTarget());
        }
    }

    private String targetAnnotationName() {
        return targetAnnotation().getCanonicalName();
    }

    protected abstract Class<? extends Annotation> targetAnnotation();

    protected abstract OperatorTarget operatorTarget();
}
