package com.fteotini.amf.commons.tester;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUnderTest implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Class<?> belongingClass;
    private final String methodSimpleName;
    private final Class<?>[] methodParamTypes;

    public MethodUnderTest(final Class<?> belongingClass, final Method method) {
        Preconditions.checkArgument(methodBelongsToClass(method, belongingClass), "The provided method does not belong to the provided class");
        this.belongingClass = belongingClass;
        this.methodSimpleName = method.getName();
        this.methodParamTypes = method.getParameterTypes();
    }

    public Class<?> getBelongingClass() {
        return belongingClass;
    }

    public Method getMethod() {
        Method method = null;
        try {
            method = belongingClass.getDeclaredMethod(methodSimpleName, methodParamTypes);
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }

    private boolean methodBelongsToClass(Method method, Class<?> aClass) {
        return Arrays.asList(aClass.getDeclaredMethods()).contains(method);
    }
}
