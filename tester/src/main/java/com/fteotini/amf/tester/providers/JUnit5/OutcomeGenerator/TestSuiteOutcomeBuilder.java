package com.fteotini.amf.tester.providers.JUnit5.OutcomeGenerator;

import com.fteotini.amf.tester.outcomes.TestEntity;
import com.fteotini.amf.tester.outcomes.TestSuiteOutcome;

import java.util.Collections;

class TestSuiteOutcomeBuilder {
    TestSuiteOutcome build(TestNode testsTreeRoot) {
        var set = testsTreeRoot.children.stream().map(node -> TestEntity.)
        return new TestSuiteOutcome(set);
    }
}
