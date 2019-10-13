package com.fteotini.amf.mutator.MutationIdentifiers;

public class FieldIdentifier implements Identifier {
    private final String fieldSimpleName;
    private final ClassIdentifier belongingClass;

    public FieldIdentifier(String fieldSimpleName, ClassIdentifier belongingClass) {
        this.fieldSimpleName = fieldSimpleName;
        this.belongingClass = belongingClass;
    }

    public ClassIdentifier getBelongingClass() {
        return belongingClass;
    }

    @Override
    public String getName() {
        return fieldSimpleName;
    }
}