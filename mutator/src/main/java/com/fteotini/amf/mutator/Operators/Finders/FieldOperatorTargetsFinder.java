package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.IMutationTarget;
import com.fteotini.amf.mutator.MutationTarget;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.FieldInfo;

import java.util.stream.Stream;

public class FieldOperatorTargetsFinder extends OperatorTargetsFinderBase<FieldInfo> {
    @Override
    protected final IMutationTarget getMutationDetails(FieldInfo entityInfo) {
        return MutationTarget.ForField(entityInfo.getName(), entityInfo.getClassInfo().getName());
    }

    @Override
    protected final boolean initialClassFilter(ClassInfo classInfo) {
        return classInfo.hasFieldAnnotation(targetAnnotationName());
    }

    @Override
    protected final Stream<FieldInfo> toResultTypeStream(ClassInfoList classInfoList) {
        return classInfoList.stream()
                .flatMap(classInfo -> classInfo.getFieldInfo().stream())
                .filter(fieldInfo -> fieldInfo.hasAnnotation(targetAnnotationName()));
    }
}
