package com.fteotini.Xavier.launcher.minion;

import com.fteotini.Xavier.launcher.MinionInputStreamHandler;
import com.fteotini.Xavier.launcher.MinionOutputStreamHandler;
import com.fteotini.Xavier.tester.TestDiscoveryOptions;
import com.fteotini.Xavier.tester.providers.JUnit5.JUnit5TestRunnerFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Main class of the forked JVM
 *
 * @author Federico Teotini (teotini.federico@gmail.com)
 */
public class MinionEntryPoint {
    private final MinionInputStreamHandler inputStreamHandler;
    private final MinionOutputStreamHandler outputStreamHandler;

    private MinionEntryPoint(MinionInputStreamHandler inputStreamHandler, MinionOutputStreamHandler outputStreamHandler) {
        this.inputStreamHandler = inputStreamHandler;
        this.outputStreamHandler = outputStreamHandler;
    }

    public static void main(String[] args) {
        ByteBuddyAgent.install();
        var minion = new MinionEntryPoint(new MinionInputStreamHandler(System.in), new MinionOutputStreamHandler(System.out));
        try {
            minion.run();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private TestDiscoveryOptions buildDiscoveryOptions(MinionArgs args) {
        var discoveryOptions = new TestDiscoveryOptions(args.getTestExecutionMode());

        args.getClassNamePatterns().ifPresent(discoveryOptions::withIncludedClassNamePatterns);
        args.getPackageNames().ifPresent(discoveryOptions::withIncludedPackageNames);
        args.getMethodUnderTest().ifPresent(methodUnderTest -> discoveryOptions.withSelectedMethods(Set.of(methodUnderTest)));

        return discoveryOptions;
    }

    private void run() throws IOException, ClassNotFoundException {
        var args = inputStreamHandler.readObject(MinionArgs.class);
        var mutator = args.getMutator();

        var mutationResults = new ArrayList<MutationResult>();

        try (var operator = mutator.makeOperator(new ByteBuddy())) {
            operator.runMutation(mutator.getMutationDetails());

            var testRunner = new JUnit5TestRunnerFactory().createTestRunner(buildDiscoveryOptions(args));
            var testExecutionSummary = testRunner.run();

            mutationResults.add(new MutationResult(mutator.getMutationDetails(), mutator.getUniqueMutationOperationId(), testExecutionSummary.getTestContainers()));
        }

        outputStreamHandler.writeObject(mutationResults.toArray(MutationResult[]::new));
    }
}