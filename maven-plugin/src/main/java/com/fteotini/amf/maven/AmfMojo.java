package com.fteotini.amf.maven;

import com.fteotini.amf.launcher.MinionProcessBuilder;
import com.fteotini.amf.launcher.minion.MinionArgs;
import com.fteotini.amf.launcher.minion.MinionResult;
import com.fteotini.amf.launcher.minion.MutationResult;
import com.fteotini.amf.mutator.Container.MutatorsContainer;
import com.fteotini.amf.mutator.IMutationTarget;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mojo(name = "mutateAnnotatationsTest", defaultPhase = LifecyclePhase.VERIFY, requiresDependencyResolution = ResolutionScope.TEST)
public class AmfMojo extends AbstractMojo {
    private static final String HORIZONTAL_RULE = "-".repeat(20);

    /**
     * <i>Internal</i>: Project to interact with.
     */
    @Parameter(property = "project", readonly = true, required = true)
    private MavenProject project;

    /**
     * <i>Internal</i>: Project to interact with.
     */
    @Parameter(property = "plugin.artifactMap", readonly = true, required = true)
    private Map<String, Artifact> pluginArtifactMap;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var currentThread = Thread.currentThread();

        var currentCL = currentThread.getContextClassLoader();
        ClassLoader newCL;
        try {
            newCL = buildNewClassLoader(currentCL);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException("Could not create the new ClassLoader", e);
        }

        try {
            currentThread.setContextClassLoader(newCL);
            List<MinionResult> jvmResults = runMutationTests();

            //TODO: manage jvm errors. See com.fteotini.amf.launcher.process.communication.ProcessCommunicationHandler and others todo
            var mutants = jvmResults.stream()
                    .flatMap(r -> r.getMutationResults().stream())
                    .collect(Collectors.toList());

            printResults(mutants);
        } finally {
            currentThread.setContextClassLoader(currentCL);
        }
    }

    private static URL toURL(String path) {
        try {
            return Path.of(path).toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void printResults(List<MutationResult> mutants) {
        var survivedMutants = survivedMutants(mutants);
        if (survivedMutants.isEmpty()) {
            getLog().info("[SUCCESS] No mutants survived. Your test suite killed them all!");
        } else {
            getLog().warn("[FAIL] The following mutants survived!");
            for (MutationResult survivedMutant : survivedMutants) {
                printSurvivedMutant(survivedMutant);
            }
        }
        getLog().info(HORIZONTAL_RULE);
        getLog().info("Mutation Score: " + calcMutationScore(mutants, survivedMutants));
    }

    private List<MinionResult> runMutationTests() throws MojoExecutionException {
        var container = MutatorsContainer.loadMutatorModules();

        List<CompletableFuture<MinionResult>> resultFutures = new ArrayList<>();
        //TODO: the mutators should be grouped and each group should be executed inside one JVM
        for (var mutator : container.getAll()) {
            getLog().info(mutator.getUniqueMutationOperationId());
            var args = MinionArgs.ForEntireSuite(mutator);
            try {
                resultFutures.add(new MinionProcessBuilder(args).start());
            } catch (IOException e) {
                throw new MojoExecutionException("Forking error", e);
            }
        }
        return resultFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private int calcMutationScore(List<MutationResult> mutants, List<MutationResult> survivedMutants) {
        return (mutants.size() - survivedMutants.size()) / mutants.size();
    }

    private void printSurvivedMutant(MutationResult survivedMutant) {
        getLog().warn(HORIZONTAL_RULE);
        getLog().warn("Mutation:");
        getLog().warn("\t" + survivedMutant.getUniqueMutationOperationId());
        getLog().warn("Mutation target:");
        getLog().warn(buildTargetInfoString(survivedMutant.getMutationTarget()));
    }

    private CharSequence buildTargetInfoString(IMutationTarget mutationTarget) {
        var targetString = "\t[Class] " + mutationTarget.getClassIdentifier().orElseThrow().getName();

        if (mutationTarget.getMethodIdentifier().isPresent()) {
            var methodIdentifier = mutationTarget.getMethodIdentifier().get();
            targetString += "\n\t[Method] " + methodIdentifier.getName() + "(" + String.join(",", methodIdentifier.getParametersType()) + ")";
        }
        if (mutationTarget.getFieldIdentifier().isPresent()) {
            var fieldIdentifier = mutationTarget.getFieldIdentifier().get();
            targetString += "\n\t[Field] " + fieldIdentifier.getName();
        }

        return targetString;
    }

    private List<MutationResult> survivedMutants(List<MutationResult> mutants) {
        return mutants.stream().filter(MutationResult::isSurvived).collect(Collectors.toList());
    }

    private ClassLoader buildNewClassLoader(ClassLoader currentCL) throws DependencyResolutionRequiredException {
        var urls = Stream.concat(this.project.getTestClasspathElements().stream(), artifactsClassPath().stream())
                .map(AmfMojo::toURL).distinct().toArray(URL[]::new);

        return URLClassLoader.newInstance(urls, currentCL);
    }

    private List<String> artifactsClassPath() {
        return this.pluginArtifactMap.values().stream().map(artifact -> artifact.getFile().getAbsolutePath()).collect(Collectors.toList());
    }
}
