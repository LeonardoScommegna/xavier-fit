package com.fteotini.amf.launcher.minion;

import java.io.IOException;

public class MinionEntryPoint {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Thread.sleep(2000);
        System.out.println("wewe");
        /*DTO obj;
        try (var reader = new ObjectInputStream(System.in)){
            obj = (DTO) reader.readObject();
            //System.out.println("input: "+obj.clazz.getCanonicalName());
        }

        var runner = new JUnit5TestRunnerFactory().createTestRunner(Set.of(Path.of("D:/Code/SWAM/amf/tester/target/test-classes")));

        var result = runner.runEntireSuite();
        try(var obj2 = new ObjectOutputStream(System.out)) {
            obj2.writeObject(result);
            obj2.flush();
        }
//        System.out.println("lol");*/
    }
}