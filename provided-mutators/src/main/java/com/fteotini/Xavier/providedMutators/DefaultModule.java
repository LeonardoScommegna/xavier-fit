package com.fteotini.Xavier.providedMutators;

import com.fteotini.Xavier.mutator.Container.MutatorModule;
import com.fteotini.Xavier.mutator.MutatorsBuilder;
import com.fteotini.Xavier.providedMutators.CDI.SwitchSessionToRequestScopedOnType;
import com.fteotini.Xavier.providedMutators.JPA.RemoveGeneratedValueOnField;
import com.fteotini.Xavier.providedMutators.JaxRS.ReplaceProducesValueOnMethod;

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
