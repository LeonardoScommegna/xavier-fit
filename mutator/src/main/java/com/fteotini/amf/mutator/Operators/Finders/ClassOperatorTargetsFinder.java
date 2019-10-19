package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.IMutationTarget;
import com.fteotini.amf.mutator.MutationTarget;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

import java.util.stream.Stream;

class ClassOperatorTargetsFinder extends OperatorTargetsFinderBase<ClassInfo> {
    @Override
    protected final IMutationTarget getMutationDetails(ClassInfo entityInfo) {
        return MutationTarget.ForClass(entityInfo.getName());
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
