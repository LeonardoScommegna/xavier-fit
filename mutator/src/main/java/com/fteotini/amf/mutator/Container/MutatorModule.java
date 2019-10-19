package com.fteotini.amf.mutator.Container;

import com.fteotini.amf.mutator.MutatorsBuilder;

import java.util.List;

public interface MutatorModule {
    List<MutatorsBuilder> registerAdditionalMutators();
}
