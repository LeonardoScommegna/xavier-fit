package com.fteotini.amf.tester.providers.JUnit5;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

class DiscoveryRequestBuilder {
    LauncherDiscoveryRequest build(DiscoveryRequestOptions options){
        return LauncherDiscoveryRequestBuilder.request()
                .selectors(buildSelectors(options))
                .filters(buildFilters(options))
                .build();
    }

    private Filter<?>[] buildFilters(DiscoveryRequestOptions options) {
        return new Filter[0];
    }

    private List<? extends DiscoverySelector> buildSelectors(DiscoveryRequestOptions options) {
        List<? extends DiscoverySelector> selectors = Collections.emptyList();

        if (!options.getAdditionalClassPaths().isEmpty()){
            selectors = selectClasspathRoots(options.getAdditionalClassPaths());
        }

        return selectors;
    }
}
