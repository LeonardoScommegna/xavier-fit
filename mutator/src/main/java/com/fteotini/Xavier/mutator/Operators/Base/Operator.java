package com.fteotini.Xavier.mutator.Operators.Base;

import com.fteotini.Xavier.mutator.IMutationTarget;

import java.io.Closeable;

public interface Operator extends AutoCloseable, Closeable {
    void runMutation(IMutationTarget mutation);
}
