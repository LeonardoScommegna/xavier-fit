package com.fteotini.amf.mutator.MutationIdentifiers;

import java.io.Serializable;

public class ClassIdentifier implements Identifier, Serializable {
    private static final long serialVersionUID = 42L;
    private final String fullName;

    public ClassIdentifier(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String getName() {
        return fullName;
    }
}
