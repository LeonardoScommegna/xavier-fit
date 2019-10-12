package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class OperatorBase<A extends Annotation, I> implements Operator {
    @Override
    public Set<MutationDetails> findMutations(ScanResult scanResult) {
        var classes = scanResult.getAllStandardClasses().filter(this::initialClassFilter);

        return toResultTypeStream(classes)
                .map(this::getMutationDetails)
                .collect(Collectors.toUnmodifiableSet());
    }

    final String targetAnnotationName() {
        return targetAnnotation().getCanonicalName();
    }

    protected abstract Class<A> targetAnnotation();

    protected abstract MutationDetails getMutationDetails(I entityInfo);

    protected abstract boolean initialClassFilter(ClassInfo classInfo);

    protected abstract Stream<I> toResultTypeStream(ClassInfoList classInfoList);
}

