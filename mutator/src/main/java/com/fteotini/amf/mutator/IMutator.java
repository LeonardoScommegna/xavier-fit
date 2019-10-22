package com.fteotini.amf.mutator;

import com.fteotini.amf.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;

import java.io.Serializable;

public interface IMutator extends Serializable {
    IMutationTarget getMutationDetails();

    String getUniqueMutationOperationId();

    Operator makeOperator(ByteBuddy buddy);
}
