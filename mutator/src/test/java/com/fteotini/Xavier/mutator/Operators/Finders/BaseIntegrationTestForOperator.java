package com.fteotini.Xavier.mutator.Operators.Finders;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;

@Tag("IntegrationTest")
abstract class BaseIntegrationTestForOperator {
    private static final String PKG_ID = "com.fteotini.Xavier.mutator.Operators.operatorTestEntities";
    static ScanResult scanResult;

    @BeforeAll
    static void setUp() {
        scanResult = new ClassGraph()
                .enableAllInfo()
                .whitelistPackages(PKG_ID)
                .scan();
    }

    @AfterAll
    static void tearDown() {
        scanResult.close();
    }

    static String getClassFullName(String className) {
        return PKG_ID + "." + className;
    }
}
