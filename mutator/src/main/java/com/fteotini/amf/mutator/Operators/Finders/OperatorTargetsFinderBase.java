package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.IMutationTarget;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class OperatorTargetsFinderBase<I> implements OperatorTargetsFinder {
    private Class<? extends Annotation> annotationClass;

    @Override
    public Set<IMutationTarget> findMutations(ScanResult scanResult, Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
        var classes = scanResult.getAllStandardClasses().filter(this::initialClassFilter);

        return toResultTypeStream(classes)
                .map(this::getMutationDetails)
                .collect(Collectors.toUnmodifiableSet());
    }

    final String targetAnnotationName() {
        return annotationClass.getCanonicalName();
    }

    protected abstract IMutationTarget getMutationDetails(I entityInfo);

    protected abstract boolean initialClassFilter(ClassInfo classInfo);

    protected abstract Stream<I> toResultTypeStream(ClassInfoList classInfoList);
}

