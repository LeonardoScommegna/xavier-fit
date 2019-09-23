package com.fteotini.amf.launcher.minion;

import com.fteotini.amf.launcher.MinionInputStreamHandler;
import com.fteotini.amf.launcher.MinionOutputStreamHandler;
import com.fteotini.amf.tester.ExecutionSummary.TestExecutionSummary;
import com.fteotini.amf.tester.TestRunner;
import com.fteotini.amf.tester.providers.JUnit5.JUnit5TestRunnerFactory;

import java.io.IOException;
import java.util.Collections;

public class MinionEntryPoint {
    private final MinionInputStreamHandler inputStreamHandler;
    private final MinionOutputStreamHandler outputStreamHandler;

    private MinionEntryPoint(MinionInputStreamHandler inputStreamHandler, MinionOutputStreamHandler outputStreamHandler) {
        this.inputStreamHandler = inputStreamHandler;
        this.outputStreamHandler = outputStreamHandler;
    }

    private void run() throws IOException, ClassNotFoundException {
        var args = inputStreamHandler.readObject(MinionArgs.class);

        TestRunner testRunner;
        TestExecutionSummary testExecutionSummary = null;
        switch (args.getTestExecutionMode()){
            case ENTIRE_SUITE:
                testRunner = new JUnit5TestRunnerFactory().createTestRunner(Collections.emptySet());
                testExecutionSummary = testRunner.runEntireSuite();
                break;
            case SINGLE_METHOD:
                break;
        }

        outputStreamHandler.writeObject(testExecutionSummary);
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