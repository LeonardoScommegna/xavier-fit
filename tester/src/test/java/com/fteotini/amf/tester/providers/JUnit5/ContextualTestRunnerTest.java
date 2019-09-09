package com.fteotini.amf.tester.providers.JUnit5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
class ContextualTestRunnerTest {
    @Mock
    private Thread currentThread;
    @Mock
    private ClassLoader dummyClassLoader;

    @Captor
    private ArgumentCaptor<URLClassLoader> customClassLoaderCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(currentThread.getContextClassLoader()).thenReturn(dummyClassLoader);
    }

    @Test
    void Given_a_supplier_it_should_return_its_result() {
        var sut = buildSut();

        var result = sut.run(() -> 8);

        assertThat(result).isEqualTo(8);
    }

    @Test
    void Given_a_set_of_class_paths_then_it_should_build_a_new_class_loader_and_set_it_as_the_current_thread_class_loader() throws MalformedURLException {
        var paths = Set.of(Path.of("/path1"), Path.of("/path2"));
        var sut = buildSut(paths);

        sut.run(() -> 0);

        verify(currentThread, atLeastOnce()).setContextClassLoader(customClassLoaderCaptor.capture());

        var newClassLoader = customClassLoaderCaptor.getAllValues().get(0);
        assertThat(newClassLoader.getParent()).isEqualTo(dummyClassLoader);
        assertThat(newClassLoader.getURLs()).hasSize(2);
        assertThat(newClassLoader.getURLs()).anySatisfy(url -> assertThat(url.toString()).endsWith("/path1"));
        assertThat(newClassLoader.getURLs()).anySatisfy(url -> assertThat(url.toString()).endsWith("/path2"));
    }

    @Test
    void Given_a_not_throwing_supplier_then_it_should_reset_the_class_loader_to_the_original_one() {
        buildSut().run(() -> 1);

        verify(currentThread).setContextClassLoader(dummyClassLoader);
    }

    @Test
    void Given_a_throwing_supplier_then_it_should_reset_the_class_loader_to_the_original_one() {
        assertThatExceptionOfType(Exception.class).isThrownBy(() -> buildSut().run(() -> {
            throw new RuntimeException();
        }));

        verify(currentThread).setContextClassLoader(dummyClassLoader);
    }

    @SuppressWarnings("unchecked")
    @Test
    void Running_should_call_the_correctMethods_in_order() {
        var supplier = mock(Supplier.class, AdditionalAnswers.delegatesTo((Supplier<Integer>) () -> 1));

        buildSut().run(supplier);

        var orderVerifier = inOrder(currentThread, supplier);
        orderVerifier.verify(currentThread).getContextClassLoader();
        orderVerifier.verify(currentThread).setContextClassLoader(any(URLClassLoader.class));
        orderVerifier.verify(supplier).get();
        orderVerifier.verify(currentThread).setContextClassLoader(dummyClassLoader);
    }

    private ContextualTestRunner buildSut() {
        return buildSut(Collections.emptySet());
    }

    private ContextualTestRunner buildSut(Set<Path> classPaths) {
        return new ContextualTestRunner(classPaths, () -> currentThread);
    }
}