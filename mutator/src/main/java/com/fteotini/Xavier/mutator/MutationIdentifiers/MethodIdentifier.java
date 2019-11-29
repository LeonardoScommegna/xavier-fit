package com.fteotini.Xavier.mutator.MutationIdentifiers;

import java.io.Serializable;

public class MethodIdentifier implements Identifier, Serializable {
    private static final long serialVersionUID = 42L;

    private final String methodSimpleName;
    private final String[] parametersType;
    private final ClassIdentifier belongingClass;

    public MethodIdentifier(String methodSimpleName, String[] parametersType, ClassIdentifier belongingClass) {
        this.methodSimpleName = methodSimpleName;
        this.parametersType = parametersType;
        this.belongingClass = belongingClass;
    }

    @Override
    public String getName() {
        return methodSimpleName;
    }

    public String[] getParametersType() {
        return parametersType;
    }

    public ClassIdentifier getBelongingClass() {
        return belongingClass;
    }
}
