package com.fteotini.amf.maven;

import com.fteotini.amf.launcher.MinionProcessBuilder;
import com.fteotini.amf.launcher.minion.MinionArgs;
import com.fteotini.amf.launcher.minion.MinionResult;
import com.fteotini.amf.launcher.minion.MutationResult;
import com.fteotini.amf.mutator.Container.MutatorsContainer;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Mojo(name = "mutateAnnotatationsTest", defaultPhase = LifecyclePhase.VALIDATE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AmfMojo extends AbstractMojo {
    /**
     * <i>Internal</i>: Project to interact with.
     */
    @Parameter(property = "project", readonly = true, required = true)
    private MavenProject project;

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

            var jvmResults = resultFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            //TODO: manage jvm errors. See com.fteotini.amf.launcher.process.communication.ProcessCommunicationHandler and others todo
            //if (jvmResults.stream().anyMatch(r -> r.getProcessResult().getExitValue() != 0))
            var mutationResultsByTarget = jvmResults.stream().flatMap(r -> r.getMutationResults().stream()).collect(Collectors.groupingBy(MutationResult::getMutationTarget));
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

    private ClassLoader buildNewClassLoader(ClassLoader currentCL) throws DependencyResolutionRequiredException {
        var urls = this.project.getCompileClasspathElements()
                .stream()
                .map(AmfMojo::toURL).toArray(URL[]::new);

        return URLClassLoader.newInstance(urls, currentCL);
    }
}
