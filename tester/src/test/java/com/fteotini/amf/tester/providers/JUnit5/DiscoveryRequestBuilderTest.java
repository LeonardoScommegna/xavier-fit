package com.fteotini.amf.tester.providers.JUnit5;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClasspathRootSelector;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DiscoveryRequestBuilderTest {

    @Test
    void Given_a_suite_built_option_obj_then_it_should_build_the_right_DiscoveryRequest() {
        // TODO: Must specify paths referring to existing file
        var paths = Set.of(Path.of("/path1"), Path.of("/path2"));
        var classPatterns = Set.of("pattern1","pattern2");

        var result = new DiscoveryRequestBuilder().build(DiscoveryRequestOptions.ForEntireSuite(classPatterns,paths));

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);
        assertThat(classSelectors).hasSize(2);
        assertThat(classSelectors).anySatisfy(selector -> assertThat(selector.toString().endsWith("/path1")));
        assertThat(classSelectors).anySatisfy(selector -> assertThat(selector.toString().endsWith("/path2")));

        var patternFilters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(patternFilters).hasSize(2);
        assertThat(patternFilters).anySatisfy(filter -> assertThat(filter.toString()).isEqualTo("pattern1"));
        assertThat(patternFilters).anySatisfy(filter -> assertThat(filter.toString()).isEqualTo("pattern2"));
    }
}