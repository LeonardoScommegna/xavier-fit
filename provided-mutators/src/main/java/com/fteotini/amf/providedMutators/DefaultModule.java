package com.fteotini.amf.providedMutators;

import com.fteotini.amf.mutator.Container.MutatorModule;
import com.fteotini.amf.mutator.MutatorsBuilder;
import com.fteotini.amf.providedMutators.CDI.SwitchSessionToRequestScopedOnType;

import java.util.List;

public class DefaultModule implements MutatorModule {
    @Override
    public List<MutatorsBuilder> registerAdditionalMutators() {
        return List.of(
                new SwitchSessionToRequestScopedOnType()
        );
    }
}
