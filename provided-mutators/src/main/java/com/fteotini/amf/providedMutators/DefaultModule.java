package com.fteotini.amf.providedMutators;

import com.fteotini.amf.mutator.Container.MutatorModule;
import com.fteotini.amf.mutator.MutatorsBuilder;
import com.fteotini.amf.providedMutators.CDI.SwitchSessionToRequestScopedOnType;
import com.fteotini.amf.providedMutators.JPA.RemoveGeneratedValueOnField;
import com.fteotini.amf.providedMutators.JaxRS.ReplaceProducesValueOnMethod;

import java.util.List;

public class DefaultModule implements MutatorModule {
    @Override
    public List<MutatorsBuilder> registerAdditionalMutators() {
        return List.of(
                new SwitchSessionToRequestScopedOnType(),
                new ReplaceProducesValueOnMethod(),
                new RemoveGeneratedValueOnField()
        );
    }
}
