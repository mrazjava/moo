package pl.zimowski.moo.client.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientHandler} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientHandlerTest extends MooTest {

    @InjectMocks
    private ClientHandler handler;

    @Mock
    private ConnectionManagement manager;

    @Mock
    private ClientReporting reporter;


    @Test
    public void shouldSendClientEvent() {

        SendClientEventAnswer answer = new SendClientEventAnswer();
        doAnswer(answer).when(manager).send(Mockito.any());

        assertNull(answer.event);
        handler.send(new ClientEvent(ClientAction.Signin));
        assertNotNull(answer.event);
        assertEquals(ClientAction.Signin, answer.event.getAction());
    }

    @Test
    public void shouldConnect() {

        when(manager.connect(Mockito.any())).thenReturn(true);
        assertTrue(handler.connect(reporter));
    }

    @Test
    public void shouldReportConnectionStatus() {

        when(manager.isConnected()).thenReturn(true);
        assertTrue(handler.isConnected());
    }

    @Test
    public void shouldDisconnect() {

        DisconnectAnswer answer = new DisconnectAnswer();
        doAnswer(answer).when(manager).disconnect();

        assertFalse(answer.disconnected);
        handler.disconnect();
        assertTrue(answer.disconnected);
    }

    class SendClientEventAnswer implements Answer<ClientEvent> {

        ClientEvent event;

        @Override
        public ClientEvent answer(InvocationOnMock invocation) throws Throwable {
            event = invocation.getArgument(0);
            return null;
        }
    }

    class DisconnectAnswer implements Answer<Void> {

        boolean disconnected;

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            disconnected = true;
            return null;
        }
    }
}