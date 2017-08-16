package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Ensures that {@link ClientEvent} correctly encapsulates information.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientEventTest {

    @Test
    public void shouldProduceEventVia1ArgConstructor() {

        ClientEvent event = new ClientEvent(ClientAction.Signin);

        assertTrue(event.getTimestamp() > 0);
        assertEquals(ClientAction.Signin, event.getAction());
        assertNull(event.getAuthor());
        assertNull(event.getMessage());

        event.withAuthor("foo").withMessage("bar");

        assertEquals("foo", event.getAuthor());
        assertEquals("bar", event.getMessage());
    }

    @Test
    public void shouldProduceEventVia2ArgConstructor() {

        ClientEvent event = new ClientEvent(ClientAction.Message, "foo", "bar");

        assertTrue(event.getTimestamp() > 0);
        assertEquals(ClientAction.Message, event.getAction());
        assertEquals("foo", event.getAuthor());
        assertEquals("bar", event.getMessage());
    }
}