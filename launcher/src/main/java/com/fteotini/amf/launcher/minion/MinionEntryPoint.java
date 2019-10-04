package com.fteotini.amf.launcher.minion;

import com.fteotini.amf.launcher.MinionInputStreamHandler;
import com.fteotini.amf.launcher.MinionOutputStreamHandler;
import com.fteotini.amf.tester.TestDiscoveryOptions;
import com.fteotini.amf.tester.providers.JUnit5.JUnit5TestRunnerFactory;

import java.io.IOException;
import java.util.Set;

public class MinionEntryPoint {
    private final MinionInputStreamHandler inputStreamHandler;
    private final MinionOutputStreamHandler outputStreamHandler;

    private MinionEntryPoint(MinionInputStreamHandler inputStreamHandler, MinionOutputStreamHandler outputStreamHandler) {
        this.inputStreamHandler = inputStreamHandler;
        this.outputStreamHandler = outputStreamHandler;
    }

    private void run() throws IOException, ClassNotFoundException {
        var args = inputStreamHandler.readObject(MinionArgs.class);

        var testRunner = new JUnit5TestRunnerFactory().createTestRunner(buildDiscoveryOptions(args));

        var testExecutionSummary = testRunner.run();

        outputStreamHandler.writeObject(testExecutionSummary);
    }

    private TestDiscoveryOptions buildDiscoveryOptions(MinionArgs args) {
        var discoveryOptions = new TestDiscoveryOptions(args.getTestExecutionMode());

        args.getClassNamePatterns().ifPresent(discoveryOptions::withIncludedClassNamePatterns);
        args.getPackageNames().ifPresent(discoveryOptions::withIncludedPackageNames);
        args.getMethodUnderTest().ifPresent(methodUnderTest -> discoveryOptions.withSelectedMethods(Set.of(methodUnderTest)));

        return discoveryOptions;
    }

    public static void main(String[] args) {
        var minion = new MinionEntryPoint(new MinionInputStreamHandler(System.in), new MinionOutputStreamHandler(System.out));
        try {
            minion.run();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}