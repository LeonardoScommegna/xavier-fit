package com.fteotini.amf.launcher;

public class ProcessInvoker {
    public ProcessInvoker(ProcessArgs processArgs, MinionArgs minionArgs) {
    }
    /*public static void main(String[] args) throws InterruptedException, TimeoutException, IOException, ClassNotFoundException {
        var inputMsg = new DTO(MinionEntry.class);
        var input = new InputStreamWriter()
                .writeObject(inputMsg)
                .create();

        var cl = System.getProperty("java.class.path") + ";D:/Code/SWAM/amf/launcher/target/test-classes";


        var d2 = "-agentlib:jdwp=transport=dt_socket,server=n,address=DESKTOP-TR4EENN:50005,suspend=y";

        var output = new OutputStreamReader();

        var processExecutor = new ProcessExecutor("java", d2,"-cp", cl, "DummyMain");
        var r = processExecutor
                .redirectInput(input)
                .redirectOutput(output.getOutputStream())
                .redirectError(System.err)
//                .readOutput(true)
//                .destroyOnExit()
                .execute();

        var summ = output.readObject(TestExecutionSummary.class);
        System.out.println("output: "+summ.getTestContainers().size());
        System.out.println(r.getExitValue());
        //System.out.println(r.outputString());
    }*/
}

