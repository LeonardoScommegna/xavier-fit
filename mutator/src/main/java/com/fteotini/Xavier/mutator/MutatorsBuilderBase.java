package com.fteotini.Xavier.mutator;

import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import com.fteotini.Xavier.mutator.Operators.Finders.OperatorTargetsFinder;
import io.github.classgraph.ScanResult;
import net.bytebuddy.ByteBuddy;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class MutatorsBuilderBase implements MutatorsBuilder, Serializable {
    private static final long serialVersionUID = 42L;
    private final UUID uuid = UUID.randomUUID();

    @Override
    public String uniqueMutationOperationId() {
        return mutationOperationId() + "_" + uuid.toString();
    }

    @Override
    public Set<IMutator> buildMutators(final ScanResult scanResult) {
        var details = targetsFinder().findTargets(scanResult, currentAnnotation());
        return details.stream()
                .map(detail -> new Mutator(uniqueMutationOperationId(), detail, this::operator))
                .collect(Collectors.toUnmodifiableSet());
    }

    protected abstract OperatorTargetsFinder targetsFinder();

    protected abstract Class<? extends Annotation> currentAnnotation();

    protected abstract Operator operator(ByteBuddy buddy);

    protected abstract String mutationOperationId();
}
