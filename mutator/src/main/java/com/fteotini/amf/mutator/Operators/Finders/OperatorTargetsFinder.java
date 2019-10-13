package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.MutationDetails;
import io.github.classgraph.ScanResult;

import java.util.Set;

public interface OperatorTargetsFinder {
    Set<MutationDetails> findMutations(ScanResult scanResult);
}
