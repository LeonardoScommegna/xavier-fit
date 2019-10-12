package com.fteotini.amf.mutator.Operators;

import com.fteotini.amf.mutator.MutationDetails;
import io.github.classgraph.ScanResult;

import java.util.Set;

public interface Operator {
    Set<MutationDetails> findMutations(ScanResult scanResult);
}
