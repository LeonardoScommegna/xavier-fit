package com.fteotini.amf.mutator.Visitors.ForAnnotations.ValuesReplacer;

import com.google.common.base.Preconditions;

import java.lang.reflect.Method;

class NewValueEntry {
    private final Method method;
    private final Object newValue;
    private final ValueType valueType;

    NewValueEntry(Method method, Object newValue, ValueType valueType) {
        Class<?> newValueType = newValue.getClass();
        Preconditions.checkArgument(method.getReturnType() == newValueType, "The return type of method [%s] does not math the newValue Type [%s]", method, newValueType);

        this.method = method;
        this.newValue = newValue;
        this.valueType = valueType;
    }

    Object getNewValue() {
        return newValue;
    }

    boolean isOfType(ValueType type) {
        return valueType == type;
    }

    String getName() {
        return method.getName();
    }
}
