package com.fteotini.amf.mutator;

public class MutationDetails {
    private final String annotationTargetIdentifier;
    private final OperatorTarget targetElementType;

    MutationDetails(String targetIdentifier, OperatorTarget targetElementType) {
        this.annotationTargetIdentifier = targetIdentifier;
        this.targetElementType = targetElementType;
    }

    public String getAnnotationTargetIdentifier() {
        return annotationTargetIdentifier;
    }

    public final OperatorTarget getTargetElementType() {
        return targetElementType;
    }
}
