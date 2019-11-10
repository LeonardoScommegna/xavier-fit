package com.fteotini.amf.maven.it;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Disabled
public class osTestIT {
    @Test
    void name() throws IOException, VerificationException {
        var tmpDir = ResourceExtractor.simpleExtractResources(getClass(), "/javaee-app");
        var verifiere = new Verifier(tmpDir.getAbsolutePath());
        //verifiere.setMavenDebug(true);
        verifiere.addCliOption("-DskipTests");
        verifiere.executeGoal("test");
        verifiere.setAutoclean(false);
        verifiere.setDebugJvm(true);
        verifiere.executeGoal("amf:mutateAnnotatationsTest");
    }
}
