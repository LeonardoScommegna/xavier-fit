package com.fteotini.amf.mutator.MutationIdentifiers;

public class ClassIdentifier implements Identifier {
    private final String fullName;

    public ClassIdentifier(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getName() {
        return fullName;
    }
}
