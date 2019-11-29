package com.fteotini.Xavier.tester.providers.JUnit5;

import com.fteotini.Xavier.commons.tester.MethodUnderTest;
import com.fteotini.Xavier.commons.tester.TestExecutionMode;
import com.fteotini.Xavier.tester.TestDiscoveryOptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.engine.discovery.ClasspathRootSelector;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.engine.discovery.PackageNameFilter;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

import static com.fteotini.Xavier.commons.tester.TestExecutionMode.ENTIRE_SUITE;
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
                .withExcludedClassNamePatterns(Set.of(".*ful$"));

        var result = new DiscoveryRequestBuilder(options).build();

        var filters = result.getFiltersByType(ClassNameFilter.class);
        assertThat(filters).hasSize(2);

        var excludedPatterns = filters.get(1);
        assertThat(excludedPatterns.toPredicate()).rejects("stressful");
    }

    @Test
    void Given_an_option_obj_with_included_packageName_then_the_built_DiscoveryRequest_should_contain_the_right_filters() {
        var options = new TestDiscoveryOptions(ENTIRE_SUITE)
                .withIncludedPackageNames(Set.of("org.dummy.pkg", "com.pkg"));

        var result = new DiscoveryRequestBuilder(options).build();

        var filters = result.getFiltersByType(PackageNameFilter.class);
        assertThat(filters)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(f -> {
                    assertThat(f.toPredicate()).accepts("org.dummy.pkg").accepts("com.pkg").rejects("asdhaiwsdjhajnd");
                });
    }

    @Test
    void Given_an_option_obj_with_excluded_packageName_then_the_built_DiscoveryRequest_should_contain_the_right_filters() {
        var options = new TestDiscoveryOptions(ENTIRE_SUITE)
                .withExcludedPackageNames(Set.of("org.dummy.pkg", "com.pkg"));

        var result = new DiscoveryRequestBuilder(options).build();

        var filters = result.getFiltersByType(PackageNameFilter.class);
        assertThat(filters)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(f -> {
                    assertThat(f.toPredicate()).accepts("everything").rejects("org.dummy.pkg", "com.pkg");
                });
    }

    @Test
    void Given_an_option_obj_for_entire_suite_then_the_classPathRootSelector_must_contain_the_current_classPath_directory() {
        var currentClassPath = currentClassPathDirectories();
        var options = new TestDiscoveryOptions(ENTIRE_SUITE);

        var result = new DiscoveryRequestBuilder(options).build();

        var classSelectors = result.getSelectorsByType(ClasspathRootSelector.class);

        assertThat(classSelectors).hasSizeGreaterThanOrEqualTo(currentClassPath.length);
        assertThat(classSelectors)
                .extracting(ClasspathRootSelector::getClasspathRoot)
                .contains(currentClassPath);
    }

    private static URI[] currentClassPathDirectories() {
        return Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                .map(str -> Path.of(str).toFile()).filter(File::isDirectory).map(File::toURI).toArray(URI[]::new);
    }

    static class DummyTestClass {
        void dummyTest() {
        }
    }
}