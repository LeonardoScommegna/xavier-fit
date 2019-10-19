package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.IMutationTarget;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface OperatorTargetsFinder {
    Set<IMutationTarget> findMutations(ScanResult scanResult, Class<? extends Annotation> annotationClass);
}
