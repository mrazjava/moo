package pl.zimowski.moo.client.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.jms.JmsGateway;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientHandler} operates as expected.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientHandlerTest extends MooTest {

    @InjectMocks
    private ClientHandler clientHandler;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsGateway jms;

    @Mock
    private ClientEventSender clientEventSender;

    @Mock
    private ServerEventConsumer serverEventConsumer;

    @Mock
    private ServerEventSubscriber serverEventSubscriber;


    @Test
    public void shouldConnect() {

        ArgumentCaptor<ClientEvent> clientEventCaptor = ArgumentCaptor.forClass(ClientEvent.class);

        assertTrue(clientHandler.connect(null));
        assertTrue(clientHandler.isConnected());
        verify(clientEventSender).send(clientEventCaptor.capture());

        ClientEvent connectEvent = clientEventCaptor.getValue();

        assertEquals(ClientAction.Connect, connectEvent.getAction());
        assertNotNull(UUID.fromString(connectEvent.getClientId()));
    }

    @Test
    public void shouldDisconnect() {

        ReflectionTestUtils.setField(clientHandler, "connected", true);
        clientHandler.disconnect();
        assertFalse(clientHandler.isConnected());
    }
}