package com.fteotini.amf.mutator;

public class MethodIdentifier {
    private final String methodSimpleName;
    private final String[] parametersType;
    private final ClassIdentifier belongingClass;

    public MethodIdentifier(String methodSimpleName, String[] parametersType, ClassIdentifier belongingClass) {
        this.methodSimpleName = methodSimpleName;
        this.parametersType = parametersType;
        this.belongingClass = belongingClass;
    }

    public String getMethodSimpleName() {
        return methodSimpleName;
    }

    public String[] getParametersType() {
        return parametersType;
    }

    public ClassIdentifier getBelongingClass() {
        return belongingClass;
    }
}
