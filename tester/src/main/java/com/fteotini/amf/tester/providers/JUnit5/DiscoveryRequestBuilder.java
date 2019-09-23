package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.commons.tester.TestExecutionMode;
import com.fteotini.amf.tester.TestDiscoveryOptions;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

class DiscoveryRequestBuilder {
    private TestDiscoveryOptions options;

    DiscoveryRequestBuilder(TestDiscoveryOptions options) {
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

        if (!options.getIncludedClassNamePatterns().isEmpty()) {
            filters.add(includeClassNamePatterns(options.getIncludedClassNamePatterns().toArray(String[]::new)));
        }

        return filters.toArray(Filter[]::new);
    }

    private List<? extends DiscoverySelector> buildSelectors() {
        List<? extends DiscoverySelector> selectors = Collections.emptyList();

        if (!options.getAdditionalClassPaths().isEmpty() && options.getTestExecutionMode() == TestExecutionMode.ENTIRE_SUITE) {
            selectors = selectClasspathRoots(options.getAdditionalClassPaths());
        }

        if (!options.getSelectedMethods().isEmpty() && options.getTestExecutionMode() == TestExecutionMode.SINGLE_METHOD) {
            selectors = options.getSelectedMethods().stream()
                    .map(m -> selectMethod(m.getBelongingClass(), m.getMethod()))
                    .collect(Collectors.toList());
        }

        return selectors;
    }
}
