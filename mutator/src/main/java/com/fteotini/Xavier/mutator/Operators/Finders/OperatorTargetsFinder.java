package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.IMutationTarget;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface OperatorTargetsFinder {
    Set<IMutationTarget> findTargets(ScanResult scanResult, Class<? extends Annotation> annotationClass);
}
