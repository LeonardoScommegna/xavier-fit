package com.fteotini.amf.tester.providers.JUnit5;

import com.fteotini.amf.commons.tester.MethodUnderTest;
import com.fteotini.amf.commons.tester.TestExecutionMode;
import com.fteotini.amf.tester.TestDiscoveryOptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.MethodSelector;

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

    @ParameterizedTest
    @EnumSource(value = TestExecutionMode.class, mode = EnumSource.Mode.EXCLUDE, names = "ENTIRE_SUITE")
    void Given_an_executionMode_different_from_entire_suite_then_the_built_DiscoveryRequest_should_not_contain_any_ClasspathRootSelector(TestExecutionMode executionMode) {
        var paths = Set.of(Path.of("src", "main"));

        var options = new TestDiscoveryOptions(executionMode)
                .withAdditionalClassPaths(paths);
        var result = new DiscoveryRequestBuilder(options).build();

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);
        assertThat(classSelectors).isEmpty();
    }

    @Test
    void Given_an_option_obj_for_test_methods_then_the_built_DiscoveryRequest_should_contain_the_right_selectors() throws NoSuchMethodException {
        var belongingClass = DummyTestClass.class;
        var method = belongingClass.getDeclaredMethod("dummyTest");

        var options = new TestDiscoveryOptions(TestExecutionMode.SINGLE_METHOD)
                .withSelectedMethods(Set.of(new MethodUnderTest(belongingClass, method)));
        var result = new DiscoveryRequestBuilder(options).build();

        var methodSelectors = result.getSelectorsByType(MethodSelector.class);
        assertThat(methodSelectors).hasSize(1);
        assertThat(methodSelectors.get(0).getJavaClass()).isEqualTo(belongingClass);
        assertThat(methodSelectors.get(0).getJavaMethod()).isEqualTo(method);
    }

    @Test
    void Given_an_option_obj_with_excluded_className_patterns_then_the_built_DiscoveryRequest_should_contain_the_right_filters() {
        var options = new TestDiscoveryOptions(ENTIRE_SUITE)
                .withExcludedClassNamePatterns(Set.of("one$"));

        var result = new DiscoveryRequestBuilder(options).build();
        
        var filters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(filters).hasSize(2);

        var excludedPatterns = filters.get(1);
        assertThat(excludedPatterns.toPredicate()).accepts("torrone");
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

    static class DummyTestClass {
        void dummyTest() {
        }
    }
}