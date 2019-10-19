package com.fteotini.amf.mutator.Container;

import com.fteotini.amf.mutator.Mutator;
import com.fteotini.amf.mutator.MutatorsBuilder;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.*;
import java.util.stream.Collectors;

public final class MutatorsContainer {
    private final List<MutatorsBuilder> mutatorsBuilders;
    private Map<String, Set<Mutator>> mutators;

    private MutatorsContainer(List<MutatorsBuilder> mutatorsBuilders) {
        this.mutatorsBuilders = mutatorsBuilders;
    }

    public static MutatorsContainer loadMutatorModules() {
        var mutatorsBuilders = ServiceLoader.load(MutatorModule.class)
                .stream()
                .flatMap(module -> module.get().registerAdditionalMutators().stream())
                .collect(Collectors.toUnmodifiableList());

        var loaded = new MutatorsContainer(mutatorsBuilders);
        loaded.loadMutators();
        return loaded;
    }

    public Set<Mutator> getAll() {
        return mutators.values().stream().flatMap(Collection::stream).collect(Collectors.toUnmodifiableSet());
    }

    public Set<Mutator> getMutatorsByMutationId(String mutationId) {
        return mutators.get(mutationId);
    }

    private static ScanResult buildScanResult() {
        return new ClassGraph()
                .enableAllInfo()
                .scan();
    }

    private void loadMutators() {
        try (var scanResult = buildScanResult()) {
            mutators = mutatorsBuilders.stream()
                    .collect(Collectors.toMap(MutatorsBuilder::uniqueMutationOperationId, builder -> builder.buildMutators(scanResult)));
        }
    }
}
