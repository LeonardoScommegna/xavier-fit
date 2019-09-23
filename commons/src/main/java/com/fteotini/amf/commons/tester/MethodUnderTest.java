package com.fteotini.amf.commons.tester;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUnderTest implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Class<?> belongingClass;
    private final Method method;

    MethodUnderTest(final Class<?> belongingClass, final Method method) {
        Preconditions.checkArgument(methodBelongsToClass(method, belongingClass), "The provided method does not belong to the provided class");
        this.belongingClass = belongingClass;
        this.method = method;
    }

    public Class<?> getBelongingClass() {
        return belongingClass;
    }

    public Method getMethod() {
        return method;
    }

    private boolean methodBelongsToClass(Method method, Class<?> aClass) {
        return Arrays.asList(aClass.getMethods()).contains(method);
    }
}
