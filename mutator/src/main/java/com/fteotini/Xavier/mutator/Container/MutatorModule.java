package com.fteotini.Xavier.mutator.Container;

import com.fteotini.Xavier.mutator.MutatorsBuilder;

import java.util.List;

public interface MutatorModule {
    List<MutatorsBuilder> registerAdditionalMutators();
}
