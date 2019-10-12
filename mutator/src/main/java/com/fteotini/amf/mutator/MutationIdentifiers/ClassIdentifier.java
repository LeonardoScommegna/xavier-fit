package com.fteotini.amf.mutator.MutationIdentifiers;

public class ClassIdentifier {
    private final String fullName;

    public ClassIdentifier(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
