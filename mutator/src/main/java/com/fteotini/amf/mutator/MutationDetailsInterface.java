package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.MutationIdentifiers.ClassIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.FieldIdentifier;
import com.fteotini.amf.mutator.MutationIdentifiers.MethodIdentifier;

import java.util.Optional;

public interface MutationDetailsInterface {
    OperatorTarget getTargetElementType();

    Optional<ClassIdentifier> getClassIdentifier();

    Optional<MethodIdentifier> getMethodIdentifier();

    Optional<FieldIdentifier> getFieldIdentifier();
}
