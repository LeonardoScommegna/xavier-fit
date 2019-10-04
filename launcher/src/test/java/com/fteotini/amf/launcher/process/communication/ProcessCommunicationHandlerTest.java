package com.fteotini.amf.launcher.process.communication;

import com.fteotini.amf.launcher.MinionInputStreamHandler;
import com.fteotini.amf.launcher.MinionOutputStreamHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

//TODO: decide how to handle error stream
@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ProcessCommunicationHandlerTest {
    @Mock
    private MinionOutputStreamHandler outputStreamHandler;
    @Mock
    private MinionInputStreamHandler inputStreamHandler;

    @Test
    void Given_an_outputStreamHandlerFactory_then_calling_start_should_call_it_and_pass_its_result_to_sendData_consumer() throws IOException {
        var ref = new Object() {
            int assertionsCount = 0;
        };
        var arg = OutputStream.nullOutputStream();

        Function<OutputStream, MinionOutputStreamHandler> outputStreamHandlerFactory = outputStream -> {
            assertThat(outputStream).isEqualTo(arg);
            ref.assertionsCount++;
            return outputStreamHandler;
        };
        Consumer<MinionOutputStreamHandler> sendInitialDataFunc = minionOutputStreamHandler -> {
            assertThat(minionOutputStreamHandler).isEqualTo(outputStreamHandler);
            ref.assertionsCount++;
        };

        var sut = new ProcessCommunicationHandler(outputStreamHandlerFactory, $ -> null, sendInitialDataFunc, $ -> {
        });

        sut.setProcessInputStream(arg);
        sut.start();

        assertThat(ref.assertionsCount).isEqualTo(2);
    }

    @Test
    void Given_an_inputStreamHandlerFactory_then_calling_start_should_call_it_and_pass_its_result_to_receiveData_consumer() throws IOException {
        var ref = new Object() {
            int assertionsCount = 0;
        };
        var arg = InputStream.nullInputStream();

        Function<InputStream, MinionInputStreamHandler> inputStreamHandlerFactory = outputStream -> {
            assertThat(outputStream).isEqualTo(arg);
            ref.assertionsCount++;
            return inputStreamHandler;
        };
        Consumer<MinionInputStreamHandler> receiveDataFunc = minionInputStreamHandler -> {
            assertThat(minionInputStreamHandler).isEqualTo(inputStreamHandler);
            ref.assertionsCount++;
        };

        var sut = new ProcessCommunicationHandler($ -> null, inputStreamHandlerFactory, $ -> {
        }, receiveDataFunc);

        sut.setProcessOutputStream(arg);
        sut.start();

        assertThat(ref.assertionsCount).isEqualTo(2);
    }
}