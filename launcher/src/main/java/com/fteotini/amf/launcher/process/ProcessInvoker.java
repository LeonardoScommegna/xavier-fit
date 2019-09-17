package com.fteotini.amf.launcher.process;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.ExecuteStreamHandler;

import java.util.function.Supplier;

public class ProcessInvoker {
    private final ProcessArgs processArgs;
    private final ExecuteStreamHandler processCommunicationHandler;
    private final Supplier<ProcessExecutor> processExecutorSupplier;

    public ProcessInvoker(ProcessArgs processArgs, ExecuteStreamHandler processCommunicationHandler) {
        this(processArgs,processCommunicationHandler, ProcessExecutor::new);
    }

    ProcessInvoker(ProcessArgs processArgs, ExecuteStreamHandler processCommunicationHandler, Supplier<ProcessExecutor> processExecutorSupplier) {
        this.processArgs = processArgs;
        this.processCommunicationHandler = processCommunicationHandler;
        this.processExecutorSupplier = processExecutorSupplier;
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

