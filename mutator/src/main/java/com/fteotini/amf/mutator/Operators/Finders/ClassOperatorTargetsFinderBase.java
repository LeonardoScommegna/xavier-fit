package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.MutationDetails;
import com.fteotini.amf.mutator.MutationDetailsInterface;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

abstract class ClassOperatorTargetsFinderBase<A extends Annotation> extends OperatorTargetsFinderBase<A, ClassInfo> {
    @Override
    protected final MutationDetailsInterface getMutationDetails(ClassInfo entityInfo) {
        return MutationDetails.ForClass(entityInfo.getName());
    }

    @Override
    protected final boolean initialClassFilter(ClassInfo classInfo) {
        return classInfo.hasAnnotation(targetAnnotationName());
    }

    @Override
    protected final Stream<ClassInfo> toResultTypeStream(ClassInfoList classInfoList) {
        return classInfoList.stream();
    }
}
