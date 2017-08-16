package pl.zimowski.moo.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.MockLogger;

/**
 * Ensures that {@link ChatEngine} operates as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ChatEngineTest {

    private static final Logger log = LoggerFactory.getLogger(ChatEngineTest.class);

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @InjectMocks
    private ChatEngine engine;

    @Spy
    private MockLogger mockLog;


    @Test
    public void shouldStartAndStop() throws InterruptedException {

        assertFalse(engine.isRunning());
        startEngine(8000);
        assertTrue(engine.isRunning());
        engine.stop();
        assertFalse(engine.isRunning());
    }

    @Test
    public void shouldAcceptClient() throws InterruptedException, UnknownHostException, IOException {

        int testPort = 8001;

        startEngine(testPort);
        assertEquals(0, engine.getConnectedClientCount());
        Socket socket = establishTestConnection(testPort);
        assertEquals(1, engine.getConnectedClientCount());

        engine.stop();
        Thread.sleep(500); // allow engine to shut down
        socket.close();
    }

    private void startEngine(int port) throws InterruptedException {

        engine.setPort(port);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> { engine.start(); });
        Thread.sleep(500); // allow engine to fully initialize
    }

    private Socket establishTestConnection(int port) throws InterruptedException, UnknownHostException, IOException {

        Socket socket = new Socket("localhost", port);
        log.debug("establishing test connection: {}", socket);
        Thread.sleep(500); // allow connection to fully establish a link

        return socket;
    }
}