package com.fteotini.amf.launcher.util;

import java.nio.file.Path;

public class JavaExecutableLocator {

    public Path getLocation() {
        var javaHome = System.getProperty("java.home");
        return Path.of(javaHome,"bin","java");
    }
}
