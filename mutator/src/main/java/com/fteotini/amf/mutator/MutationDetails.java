package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;

import java.util.Optional;

public final class MutationDetails {
    private final OperatorTarget targetElementType;
    private final ClassIdentifier classIdentifier;
    private final MethodIdentifier methodIdentifier;
    private final FieldIdentifier fieldIdentifier;

    private MutationDetails(OperatorTarget targetElementType, ClassIdentifier classIdentifier, MethodIdentifier methodIdentifier, FieldIdentifier fieldIdentifier) {
        this.targetElementType = targetElementType;
        this.classIdentifier = classIdentifier;
        this.methodIdentifier = methodIdentifier;
        this.fieldIdentifier = fieldIdentifier;
    }

    public static MutationDetails ForClass(String classFullName) {
        var classId = new ClassIdentifier(classFullName);
        return new MutationDetails(OperatorTarget.Class, classId, null, null);
    }

    public static MutationDetails ForMethod(String methodSimpleName, String[] parametersType, String belongingClassFullName) {
        var methodId = new MethodIdentifier(methodSimpleName, parametersType, new ClassIdentifier(belongingClassFullName));
        return new MutationDetails(OperatorTarget.Method, null, methodId, null);
    }

    public static MutationDetails ForField(String fieldName, String belongingClassFullName) {
        var fieldId = new FieldIdentifier(fieldName, new ClassIdentifier(belongingClassFullName));
        return new MutationDetails(OperatorTarget.Field, null, null, fieldId);
    }

    public OperatorTarget getTargetElementType() {
        return targetElementType;
    }

    public Optional<ClassIdentifier> getClassIdentifier() {
        return Optional.ofNullable(classIdentifier);
    }

    public Optional<MethodIdentifier> getMethodIdentifier() {
        return Optional.ofNullable(methodIdentifier);
    }

    public Optional<FieldIdentifier> getFieldIdentifier() {
        return Optional.ofNullable(fieldIdentifier);
    }
}
