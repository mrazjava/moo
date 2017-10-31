package pl.zimowski.moo.ui.shell.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Ensures that {@link AbstractClientReporter} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class AbstractClientListenerTest {

    private AbstractClientReporter listener = new AbstractClientReporter() {

        @Override
        public void onEvent(ServerEvent event) {
        }

        @Override
        public String getAuthor() {
            return "moo";
        }
    };


    @Test
    public void shouldRetainInformation() {

        listener.setNick("baboon");
        listener.onBeforeServerConnect("localhost:8001");
        listener.onConnectToServerError("ooops");
        listener.onEvent(new ServerEvent(ServerAction.ClientDisconnected));

        assertEquals("moo", listener.getAuthor());
        assertEquals("baboon", listener.getNick());
    }

    @Test
    public void shouldCreateEvent() {

        ClientEvent event = listener.newEvent(ClientAction.GenerateNick);

        assertNotNull(event);
        assertEquals(ClientAction.GenerateNick, event.getAction());
    }

    @Test
    public void shouldCreateSigninEvent() {

        ClientEvent event = listener.newSigninEvent();

        assertNotNull(event);
        assertEquals(ClientAction.Signin, event.getAction());
        assertNull(event.getAuthor());
        assertNull(event.getMessage());
    }

    @Test
    public void shouldCreateGenerateNickEvent() {

        ClientEvent event = listener.newGenerateNickEvent();

        assertNotNull(event);
        assertEquals(ClientAction.GenerateNick, event.getAction());
        assertNull(event.getAuthor());
        assertNull(event.getMessage());
    }

    @Test
    public void shouldCreateMessageEvent() {

        String clientId = "foo-bar";
        String author = "rocky";
        String message = "anybody there?";

        ReflectionTestUtils.setField(listener, "clientId", clientId);
        listener.setNick(author);

        ClientEvent event = listener.newMessageEvent(message);

        assertNotNull(event);
        assertEquals(ClientAction.Message, event.getAction());
        assertEquals(clientId, event.getClientId());
        assertEquals(author, event.getAuthor());
        assertEquals(message, event.getMessage());
    }

    @Test
    public void shouldCreateSignoffEvent() {

        String clientId = "foo-bar";
        String author = "rocky";

        ReflectionTestUtils.setField(listener, "clientId", clientId);
        listener.setNick(author);

        ClientEvent event = listener.newSignoffEvent();

        assertNotNull(event);
        assertEquals(ClientAction.Signoff, event.getAction());
        assertEquals(clientId, event.getClientId());
        assertEquals(author, event.getAuthor());
        assertNull(event.getMessage());
    }

    @Test
    public void shouldCreateDisconnectEvent() {

        String clientId = "foo-bar";

        ReflectionTestUtils.setField(listener, "clientId", clientId);

        ClientEvent event = listener.newDisconnectEvent();

        assertNotNull(event);
        assertEquals(ClientAction.Disconnect, event.getAction());
        assertEquals(clientId, event.getClientId());
    }
}