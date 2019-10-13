package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetailsInterface;

public interface Operator {
    void runMutation(MutationDetailsInterface mutation);
}
