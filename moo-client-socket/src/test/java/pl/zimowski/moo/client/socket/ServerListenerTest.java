package pl.zimowski.moo.client.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ServerListener} correctly listens for events.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerListenerTest extends MooTest {

    @Mock
    private Socket socket;

    @Mock
    private ClientReporting reporter;

    private ServerEvent testServerEvent = null;


    @Test
    public void shouldRun() throws UnknownHostException, IOException, InterruptedException {

        ServerEvent serverEvent = new ServerEvent(ServerAction.ConnectionEstablished).withMessage("hello");

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(serverEvent);
        o.close();

        //Socket socket = new Socket("localhost", 8000);
        Mockito.doReturn(false).when(socket).isInputShutdown();
        Mockito.doReturn(new ByteArrayInputStream(b.toByteArray())).when(socket).getInputStream();
        Mockito.doAnswer(new Answer<ServerEvent>() {

            @Override
            public ServerEvent answer(InvocationOnMock invocation) throws Throwable {
                testServerEvent = invocation.getArgument(0);
                return null;
            }

        }).when(reporter).onEvent(Mockito.any());

        ServerListener serverListener = new ServerListener(socket, reporter);

        assertNull(testServerEvent);
        serverListener.setHardExit(false);
        serverListener.run();
        assertNotNull(testServerEvent);
        assertEquals(ServerAction.ConnectionEstablished, testServerEvent.getAction());
    }

    @Test
    public void shouldExitOnIOException() {

        doThrow(RuntimeException.class).when(socket).isInputShutdown();
        ServerListener serverListener = new ServerListener(socket, null);

        serverListener.setHardExit(false);
        serverListener.run();
    }

    @Test
    public void shouldBeListening() {

        Mockito.doReturn(false).when(socket).isClosed();
        ServerListener listener = new ServerListener(socket, null);
        listener.setHardExit(true);

        assertTrue(listener.isListening());
    }
}
