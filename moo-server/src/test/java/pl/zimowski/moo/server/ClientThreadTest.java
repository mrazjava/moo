package pl.zimowski.moo.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Ensures operation of {@link ClientThread} is as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientThreadTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @InjectMocks
    private ClientThread clientThread;

    @Mock
    private Socket socket;

    @Spy
    private ServerNotificationMock clientThreadObserver;


    @Test
    public void shouldRunClientThreadAndExit() throws IOException {

        Object event = new ClientEvent(ClientAction.Signoff, "zorro", null);
        InputStream is = eventAsInputStream(event);

        given(socket.getInputStream()).willReturn(is);

        clientThread.run();

        assertNotNull(clientThreadObserver.getClientThread());
        assertNotNull(clientThreadObserver.getClientEvent());

        // intentionally comparing references
        assertTrue(clientThreadObserver.getClientThread() == clientThread);
    }

    @Test
    public void shouldProcessServerNotification() throws IOException, ClassNotFoundException {

        given(socket.getOutputStream()).willReturn(new ByteArrayOutputStream());
        assertTrue(clientThread.notify(new ServerEvent(ServerAction.Message, "hello there")));
    }

    private InputStream eventAsInputStream(Object event) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(event);

        return new ByteArrayInputStream(baos.toByteArray());
    }
}