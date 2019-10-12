package com.fteotini.amf.mutator;

public class FieldIdentifier {
    private final String fieldSimpleName;
    private final ClassIdentifier belongingClass;

    public FieldIdentifier(String fieldSimpleName, ClassIdentifier belongingClass) {
        this.fieldSimpleName = fieldSimpleName;
        this.belongingClass = belongingClass;
    }

    public String getFieldSimpleName() {
        return fieldSimpleName;
    }

    public ClassIdentifier getBelongingClass() {
        return belongingClass;
    }
}