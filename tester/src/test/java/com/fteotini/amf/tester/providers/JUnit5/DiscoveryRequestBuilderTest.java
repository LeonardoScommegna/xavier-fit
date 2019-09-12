package com.fteotini.amf.tester.providers.JUnit5;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClasspathRootSelector;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class DiscoveryRequestBuilderTest {

    @Test
    void Given_an_option_obj_built_for_test_suite_then_it_should_build_the_right_DiscoveryRequest() {
        var paths = Set.of(Path.of("src", "main"), Path.of("src", "test"));
        var classPatterns = Set.of("pattern.*", "class");

        var result = new DiscoveryRequestBuilder(DiscoveryRequestOptions.ForEntireSuite(classPatterns, paths)).build();

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);
        assertThat(classSelectors).hasSize(2);
        assertThat(classSelectors).anySatisfy(selector -> assertThat(selector.getClasspathRoot().toString()).endsWith("src/main/"));
        assertThat(classSelectors).anySatisfy(selector -> assertThat(selector.getClasspathRoot().toString()).endsWith("src/test/"));

        var patternFilters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(patternFilters).hasSize(1);
        assertThat(patternFilters.get(0).toPredicate().test("pattern1")).isTrue();
        assertThat(patternFilters.get(0).toPredicate().test("class")).isTrue();
        assertThat(patternFilters.get(0).toPredicate().test("classasdasdads")).isFalse();
    }

    @Test
    void Given_an_empty_suite_option_obj_then_it_should_build_the_right_DiscoveryRequest() {
        var result = new DiscoveryRequestBuilder(DiscoveryRequestOptions.ForEntireSuite(Collections.emptySet(), Collections.emptySet())).build();

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);
        assertThat(classSelectors).isEmpty();

        var patternFilters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(patternFilters).isEmpty();
    }
}