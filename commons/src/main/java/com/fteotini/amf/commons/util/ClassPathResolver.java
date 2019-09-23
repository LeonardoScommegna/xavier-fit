package com.fteotini.amf.commons.util;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassPathResolver {
    private final Set<Path> classPaths;

    public ClassPathResolver() {
        this.classPaths = getCurrentClassPathElements()
                .stream().filter(pathExists())
                .collect(Collectors.toSet());
    }

    public Set<Path> getClassPaths() {
        return classPaths;
    }

    private static Set<Path> getCurrentClassPathElements() {
        var currentClassPath = System.getProperty("java.class.path");
        var classPathElements = currentClassPath.split(File.pathSeparator);
        return Arrays.stream(classPathElements)
                .map(Path::of)
                .collect(Collectors.toSet());
    }

    private Predicate<? super Path> pathExists() {
        return p -> {
            var file = new File(p.toUri());
            return file.exists() && file.canRead();
        };
    }
}
