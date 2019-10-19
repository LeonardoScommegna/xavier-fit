package com.fteotini.amf.mutator;

import io.github.classgraph.ScanResult;

import java.util.Set;

public interface MutatorsBuilder {
    String uniqueMutationOperationId();

    Set<Mutator> buildMutators(final ScanResult scanResult);
}
