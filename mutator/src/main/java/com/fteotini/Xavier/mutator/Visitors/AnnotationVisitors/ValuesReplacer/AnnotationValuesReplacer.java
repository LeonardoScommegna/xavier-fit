package com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.ValuesReplacer;

import com.fteotini.Xavier.mutator.Visitors.AnnotationVisitors.AnnotationVisitorWrapper;
import com.fteotini.Xavier.mutator.Visitors.AsmConfig;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AnnotationValuesReplacer<K extends Annotation> implements AnnotationVisitorWrapper {
    private final String annotationDescriptor;
    private final Class<K> annotationType;
    private final List<NewValueEntry> newValueEntries;

    public AnnotationValuesReplacer(Class<K> annotationType, Map<String, Object> newValuesMap) {
        annotationDescriptor = Type.getDescriptor(annotationType);
        this.annotationType = annotationType;

        newValueEntries = newValuesMap
                .entrySet()
                .stream()
                .map(this::buildNewValue)
                .collect(Collectors.toList());

    }

    @Override
    public AnnotationVisitor wrap(Supplier<AnnotationVisitor> annotationVisitor, String descriptor, boolean visible) {
        var visitor = annotationVisitor.get();
        if (annotationDescriptor.equals(descriptor)) {
            visitor = new ValueReplacer(visitor);
        }
        return visitor;
    }

    private static boolean isArray(Class<?> type) {
        Class<?> arrayElementsType = type.getComponentType();
        return type.isArray() && (
                arrayElementsType.isAnnotation() || arrayElementsType.isEnum() || isPrimitive(arrayElementsType) || isArray(arrayElementsType)
        );
    }

    private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || type == String.class;
    }

    private NewValueEntry buildNewValue(Map.Entry<String, Object> newValueEntry) {
        var method = getMethodByName(newValueEntry.getKey());
        var newValue = newValueEntry.getValue();
        var valueType = getValueType(newValue);

        return new NewValueEntry(method, newValue, valueType);
    }

    private ValueType getValueType(Object newValue) {
        ValueType valueType;
        Class<?> newValueType = newValue.getClass();
        if (isPrimitive(newValueType)) {
            valueType = ValueType.Primitive;
        } else if (newValueType.isEnum()) {
            valueType = ValueType.Enum;
        } else if (newValueType.isAnnotation()) {
            valueType = ValueType.Annotation;
        } else if (isArray(newValueType)) {
            valueType = ValueType.Array;
        } else {
            throw new RuntimeException(String.format("The newValue type [%s] is not among the ones allowed for Annotation by Java", newValueType));
        }
        return valueType;
    }

    private Method getMethodByName(String methodName) {
        try {
            return annotationType.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<NewValueEntry> getNewValueEntriesByType(ValueType valueType) {
        return newValueEntries.stream()
                .filter(newValueEntry -> newValueEntry.isOfType(valueType))
                .collect(Collectors.toList());
    }

    private class ValueReplacer extends AnnotationVisitor {
        ValueReplacer(AnnotationVisitor parent) {
            super(AsmConfig.ASM_VERSION, parent);
        }

        @Override
        public void visitEnd() {
            visitPrimitive();
            visitEnum();
            visitArrays();
            super.visitEnd();
        }

        private void visitArrays() {
            for (NewValueEntry newValueEntry : getNewValueEntriesByType(ValueType.Array)) {
                var visitor = super.visitArray(newValueEntry.getName());
                visitArrayElements(visitor, newValueEntry.getNewValue());
                visitor.visitEnd();
            }
        }

        private void visitArrayElements(AnnotationVisitor visitor, Object newValue) {
            var arrayElements = convertToArrayOfObjs(newValue);
            for (var element : arrayElements) {
                visitor.visit(null, element);
            }
        }

        private Object[] convertToArrayOfObjs(Object obj) {
            if (obj instanceof Object[])
                return (Object[]) obj;

            var arrLength = Array.getLength(obj);
            var resultingArr = new Object[arrLength];
            for (int i = 0; i < arrLength; i++) {
                resultingArr[i] = Array.get(obj, i);
            }
            return resultingArr;
        }

        private void visitEnum() {
            for (NewValueEntry newValueEntry : getNewValueEntriesByType(ValueType.Enum)) {
                var newValue = newValueEntry.getNewValue();
                var enumType = Type.getDescriptor(newValue.getClass());

                super.visitEnum(newValueEntry.getName(), enumType, newValue.toString());
            }
        }

        private void visitPrimitive() {
            for (NewValueEntry newValueEntry : getNewValueEntriesByType(ValueType.Primitive)) {
                super.visit(newValueEntry.getName(), newValueEntry.getNewValue());
            }
        }
    }
}
