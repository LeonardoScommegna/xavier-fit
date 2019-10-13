package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;

public interface Operator {
    void runMutation(MutationDetails mutation);
}
