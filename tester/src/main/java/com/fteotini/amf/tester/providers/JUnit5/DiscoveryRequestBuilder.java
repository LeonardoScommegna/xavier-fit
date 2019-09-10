package com.fteotini.amf.tester.providers.JUnit5;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

class DiscoveryRequestBuilder {
    private DiscoveryRequestOptions options;

    DiscoveryRequestBuilder(DiscoveryRequestOptions options) {
        this.options = options;
    }

    LauncherDiscoveryRequest build() {
        return LauncherDiscoveryRequestBuilder.request()
                .selectors(buildSelectors())
                .filters(buildFilters())
                .build();
    }

    private Filter<?>[] buildFilters() {
        List<Filter<?>> filters = new ArrayList<>();

        if (!options.getClassNamePatterns().isEmpty()) {
            filters.add(includeClassNamePatterns(options.getClassNamePatterns().toArray(String[]::new)));
        }

        return filters.toArray(Filter[]::new);
    }

    private List<? extends DiscoverySelector> buildSelectors() {
        List<? extends DiscoverySelector> selectors = Collections.emptyList();

        if (!options.getAdditionalClassPaths().isEmpty()){
            selectors = selectClasspathRoots(options.getAdditionalClassPaths());
        }

        return selectors;
    }
}
