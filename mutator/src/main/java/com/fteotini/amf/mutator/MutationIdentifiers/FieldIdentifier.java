package com.fteotini.amf.mutator.MutationIdentifiers;

import java.io.Serializable;

public class FieldIdentifier implements Identifier, Serializable {
    private static final long serialVersionUID = 42L;

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