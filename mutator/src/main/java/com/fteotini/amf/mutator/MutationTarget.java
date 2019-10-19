package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;

import java.util.Optional;

public final class MutationTarget implements IMutationTarget {
    private final OperatorTarget targetElementType;
    private final ClassIdentifier classIdentifier;
    private final MethodIdentifier methodIdentifier;
    private final FieldIdentifier fieldIdentifier;

    private MutationTarget(OperatorTarget targetElementType, ClassIdentifier classIdentifier, MethodIdentifier methodIdentifier, FieldIdentifier fieldIdentifier) {
        this.targetElementType = targetElementType;
        this.classIdentifier = classIdentifier;
        this.methodIdentifier = methodIdentifier;
        this.fieldIdentifier = fieldIdentifier;
    }

    public static IMutationTarget ForClass(String classFullName) {
        var classId = new ClassIdentifier(classFullName);
        return new MutationTarget(OperatorTarget.Class, classId, null, null);
    }

    public static IMutationTarget ForMethod(String methodSimpleName, String[] parametersType, String belongingClassFullName) {
        var methodId = new MethodIdentifier(methodSimpleName, parametersType, new ClassIdentifier(belongingClassFullName));
        return new MutationTarget(OperatorTarget.Method, null, methodId, null);
    }

    public static IMutationTarget ForField(String fieldName, String belongingClassFullName) {
        var fieldId = new FieldIdentifier(fieldName, new ClassIdentifier(belongingClassFullName));
        return new MutationTarget(OperatorTarget.Field, null, null, fieldId);
    }

    @Override
    public OperatorTarget getTargetElementType() {
        return targetElementType;
    }

    @Override
    public Optional<ClassIdentifier> getClassIdentifier() {
        return Optional.ofNullable(classIdentifier);
    }

    @Override
    public Optional<MethodIdentifier> getMethodIdentifier() {
        return Optional.ofNullable(methodIdentifier);
    }

    @Override
    public Optional<FieldIdentifier> getFieldIdentifier() {
        return Optional.ofNullable(fieldIdentifier);
    }
}
