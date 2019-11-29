package com.fteotini.Xavier.mutator.Operators.Finders;

import com.fteotini.Xavier.mutator.IMutationTarget;
import com.fteotini.Xavier.mutator.MutationTarget;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

import java.util.stream.Stream;

public class ClassOperatorTargetsFinder extends OperatorTargetsFinderBase<ClassInfo> {
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
