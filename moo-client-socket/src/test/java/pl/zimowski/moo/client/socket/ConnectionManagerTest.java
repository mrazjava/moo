package pl.zimowski.moo.client.socket;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ConnectionManager} performs as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ConnectionManagerTest extends MooTest {

    @InjectMocks
    private ConnectionManager manager;

    @Spy
    private MockLogger mockLog;

    @Mock
    private SocketProducer producer;

    @Mock
    private Socket socket;

    @Mock
    private ClientReporting clientListener;

    @Mock
    private ServerListenerInitializer serverListenerInit;


    @Test
    public void shouldConnect() {

        given(producer.getSocketHost()).willReturn("localhost");
        given(producer.getSocketPort()).willReturn(8001);
        given(producer.getSocket()).willReturn(socket);

        assertFalse(manager.isConnected());
        manager.connect(clientListener);
        assertTrue(manager.isConnected());
    }

    @Test
    public void shouldFailConnectNullSocket() {

        // no need to setup mocks as by default nulls are returned

        assertFalse(manager.isConnected());
        manager.connect(clientListener);
        assertFalse(manager.isConnected());
    }

    @Test
    public void shouldDisconnect() {

        ReflectionTestUtils.setField(manager, "connected", true);

        assertTrue(manager.isConnected());
        manager.init();
        manager.cleanup();
        assertFalse(manager.isConnected());
    }

    @Test
    public void shouldNotSendEventNullSocket() {

        ReflectionTestUtils.setField(manager, "socket", null);
        manager.send(new ClientEvent(ClientAction.Signin));
    }

    @Test
    public void shouldNotSendEventOnIOException() throws IOException {

        Mockito.doThrow(IOException.class).when(socket).getOutputStream();
        manager.send(new ClientEvent(ClientAction.Signin));
    }

    @Test
    public void shouldSendEvent() throws IOException {

        given(socket.getOutputStream()).willReturn(new ByteArrayOutputStream());
        manager.send(new ClientEvent(ClientAction.Signin));
    }
}
