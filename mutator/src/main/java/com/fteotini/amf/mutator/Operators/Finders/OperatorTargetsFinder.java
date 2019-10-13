package com.fteotini.amf.mutator.Operators.Finders;

import com.fteotini.amf.mutator.MutationDetailsInterface;
import io.github.classgraph.ScanResult;

import java.util.Set;

public interface OperatorTargetsFinder {
    Set<MutationDetailsInterface> findMutations(ScanResult scanResult);
}
