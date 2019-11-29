package com.fteotini.Xavier.mutator;

import com.fteotini.Xavier.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.Xavier.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.Xavier.mutator.MutationIdentifiers.MethodIdentifier;

import java.util.Optional;

public interface IMutationTarget {
    OperatorTarget getTargetElementType();

    Optional<ClassIdentifier> getClassIdentifier();

    Optional<MethodIdentifier> getMethodIdentifier();

    Optional<FieldIdentifier> getFieldIdentifier();
}
