package com.fteotini.amf.mutator;

import io.github.classgraph.ScanResult;

import java.util.Set;

public interface MutatorsBuilder {
    String uniqueMutationOperationId();

    Set<IMutator> buildMutators(final ScanResult scanResult);
}
