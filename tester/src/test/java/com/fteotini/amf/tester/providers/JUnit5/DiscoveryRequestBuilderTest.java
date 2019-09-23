package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.tester.TestDiscoveryOptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClasspathRootSelector;

import java.nio.file.Path;
import java.util.Set;

import static com.fteotini.amf.commons.tester.TestExecutionMode.ENTIRE_SUITE;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("UnitTest")
class DiscoveryRequestBuilderTest {

    @Test
    void Given_an_option_obj_built_for_test_suite_then_it_should_build_the_right_DiscoveryRequest() {
        var paths = Set.of(Path.of("src", "main"), Path.of("src", "test"));
        var classPatterns = Set.of("pattern.*", "class");

        var options = new TestDiscoveryOptions(ENTIRE_SUITE)
                .withAdditionalClassPaths(paths)
                .withIncludedClassNamePatterns(classPatterns);
        var result = new DiscoveryRequestBuilder(options).build();

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
    @Disabled("should be useless by now")
    void Given_an_empty_suite_option_obj_then_it_should_build_the_right_DiscoveryRequest() {
        var result = new DiscoveryRequestBuilder(new TestDiscoveryOptions(ENTIRE_SUITE)).build();

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);
        assertThat(classSelectors).isEmpty();

        var patternFilters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(patternFilters).isEmpty();
    }
}