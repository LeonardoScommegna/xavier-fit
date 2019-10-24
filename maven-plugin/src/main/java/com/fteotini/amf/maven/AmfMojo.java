package com.fteotini.amf.maven;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

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

        /*List<CompletableFuture<MinionResult>> resultFutures = new ArrayList<>();
        for (var mutator : container.getAll()) {
            getLog().info(mutator.getUniqueMutationOperationId());
            var args = MinionArgs.ForEntireSuite(mutator);
            try {
                resultFutures.add(new MinionProcessBuilder(args).start());
            } catch (IOException e) {
                throw new MojoExecutionException("Forking error", e);
            }
        }

        var results = resultFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        getLog().info("end");*/
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
