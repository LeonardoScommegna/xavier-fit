package com.fteotini.amf.mutator.Operators.Base;

import com.fteotini.amf.mutator.IMutationTarget;

import java.io.Closeable;

public interface Operator extends AutoCloseable, Closeable {
    void runMutation(IMutationTarget mutation);
}
