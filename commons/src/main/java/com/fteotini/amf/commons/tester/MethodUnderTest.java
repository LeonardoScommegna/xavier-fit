package com.fteotini.amf.commons.tester;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Represents a method in a serializable way
 *
 * @author Federico Teotini (teotini.federico@gmail.com)
 */
public class MethodUnderTest implements Serializable {
    private static final long serialVersionUID = 42L;

    private final String belongingClassName;
    private final String methodSimpleName;
    private final Class<?>[] methodParamTypes;

    public MethodUnderTest(final Class<?> belongingClass, final Method method) {
        Preconditions.checkArgument(methodBelongsToClass(method, belongingClass), "The provided method does not belong to the provided class");
        this.belongingClassName = belongingClass.getTypeName();
        this.methodSimpleName = method.getName();
        this.methodParamTypes = method.getParameterTypes();
    }

    public final Class<?> getBelongingClass() {
        try {
            return Class.forName(belongingClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public final Method getMethod() {
        Method method = null;
        try {
            method = getBelongingClass().getDeclaredMethod(methodSimpleName, methodParamTypes);
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }

    private boolean methodBelongsToClass(Method method, Class<?> aClass) {
        return Arrays.asList(aClass.getDeclaredMethods()).contains(method);
    }
}
