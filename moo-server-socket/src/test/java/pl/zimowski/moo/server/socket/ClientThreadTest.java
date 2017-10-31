package pl.zimowski.moo.server.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures operation of {@link ClientThread} is as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientThreadTest extends MooTest {

    private static final Logger log = LoggerFactory.getLogger(ClientThreadTest.class);

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

        assertNotNull(UUID.fromString(clientThread.getClientId()));
        assertTrue(clientThread.getLastActivity() > 0L);

        clientThread.run();

        assertNotNull(clientThreadObserver.getClientThread());
        assertNotNull(clientThreadObserver.getClientEvent());

        // intentionally comparing references
        assertTrue(clientThreadObserver.getClientThread() == clientThread);
    }

    @Test
    public void shouldRunClientThreadAndDisconnect() throws IOException {

        Object event = new ClientEvent(ClientAction.Disconnect);
        InputStream is = eventAsInputStream(event);

        given(socket.getInputStream()).willReturn(is);

        SocketClosedAnswer answer = new SocketClosedAnswer(new ClassNotFoundException());
        Mockito.doAnswer(answer).when(socket).close();

        assertFalse(answer.closed);
        clientThread.run();
        assertTrue(answer.closed);
    }

    @Test
    public void shouldDisconnect() throws IOException {

        SocketClosedAnswer answer = new SocketClosedAnswer();
        Mockito.doAnswer(answer).when(socket).close();

        assertFalse(answer.closed);
        clientThread.disconnect();
        assertTrue(answer.closed);
    }

    @Test
    public void shouldHandleDisconnectIOException() throws IOException {

        Mockito.doThrow(IOException.class).when(socket).close();
        clientThread.disconnect();
    }

    @Test
    public void shouldProcessServerNotification() throws IOException, ClassNotFoundException {

        given(socket.getOutputStream()).willReturn(new ByteArrayOutputStream());
        assertTrue(clientThread.notify(new ServerEvent(ServerAction.Message, "hello there")));
    }

    @Test
    public void shouldHandleIOExceptionOnNotify() throws IOException {

        given(socket.getOutputStream()).willThrow(IOException.class);
        assertFalse(clientThread.notify(new ServerEvent(ServerAction.ConnectionEstablished)));
    }

    @Test
    public void shouldReportConnectionStatus() {

        given(socket.isClosed()).willReturn(false);
        assertTrue(clientThread.isConnected());

        given(socket.isClosed()).willReturn(true);
        assertFalse(clientThread.isConnected());
    }

    @Test
    public void shouldHaveFriendlyToString() {

        String logged = clientThread.toString();
        log.debug(logged);

        assertTrue(logged.startsWith(ClientThread.class.getSimpleName() + " ["));
    }

    @Test
    public void shouldNotEqual() {

        ClientThread thread1 = new ClientThread(null, null);
        ClientThread thread2 = new ClientThread(null, null);

        assertNotEquals(thread1, thread2);
    }

    @Test
    public void shouldEqual() {

        ClientThread thread1 = new ClientThread(null, null);
        ClientThread thread2 = new ClientThread(null, null);

        ReflectionTestUtils.setField(thread2, "clientId", thread1.getClientId());

        assertEquals(thread1, thread1);
        assertEquals(thread2, thread2);
        assertEquals(thread1, thread2);
    }

    private InputStream eventAsInputStream(Object event) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(event);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Mock answer to process when closing a mocked socket.
     *
     * @since 1.2.0
     * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
     */
    class SocketClosedAnswer implements Answer<Void> {

        boolean closed;

        Throwable throwable; // optionally to throw during close operation


        public SocketClosedAnswer() {
        }

        public SocketClosedAnswer(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {

            closed = true;
            if(throwable != null) throw throwable;

            return null;
        }

    }
}