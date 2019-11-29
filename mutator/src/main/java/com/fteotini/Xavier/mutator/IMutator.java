package com.fteotini.Xavier.mutator;

import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;

import java.io.Serializable;

public interface IMutator extends Serializable {
    IMutationTarget getMutationDetails();

    String getUniqueMutationOperationId();

    Operator makeOperator(ByteBuddy buddy);
}
