package com.fteotini.amf.tester;

import java.net.URISyntaxException;

public interface TestRunner {
    SuiteOutcome runEntireSuite(String classPathResource) throws URISyntaxException;
}
