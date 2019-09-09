package com.fteotini.amf.tester.providers.JUnit5;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

class DiscoveryRequestOptions {
    private final Set<String> classNamePatterns;
    private final Set<Path> additionalClassPaths;

    private DiscoveryRequestOptions(Set<String> classNamePatterns, Set<Path> additionalClassPaths) {
        this.classNamePatterns = classNamePatterns == null ? Collections.emptySet() : classNamePatterns;
        this.additionalClassPaths = additionalClassPaths == null ? Collections.emptySet() : additionalClassPaths;
    }

    public static DiscoveryRequestOptions ForEntireSuite(Set<String> classNamePatterns, Set<Path> additionalClassPaths){
        return new DiscoveryRequestOptions(classNamePatterns,additionalClassPaths);
    }

    public Set<String> getClassNamePatterns() {
        return classNamePatterns;
    }

    public Set<Path> getAdditionalClassPaths() {
        return additionalClassPaths;
    }
}
