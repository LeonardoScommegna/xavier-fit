package com.fteotini.amf.mutator.MutationIdentifiers;

public class MethodIdentifier implements Identifier {
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
