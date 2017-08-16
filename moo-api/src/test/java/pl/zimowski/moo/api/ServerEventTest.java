package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Ensures that {@link ServerEvent} correctly encapsulates information.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEventTest {

    @Test
    public void shouldProduceEventVia1ArgConstructor() {

        ServerEvent event = new ServerEvent(ServerAction.ParticipantCount);

        assertTrue(event.getTimestamp() > 0);
        assertEquals(ServerAction.ParticipantCount, event.getAction());
        assertEquals(0, event.getParticipantCount());
        assertNull(event.getMessage());

        event.withParticipantCount(2).withMessage("bar");

        assertEquals(2, event.getParticipantCount());
        assertEquals("bar", event.getMessage());
    }

    @Test
    public void shouldProduceEventVia2ArgConstructor() {

        ServerEvent event = new ServerEvent(ServerAction.Message, "hello world");

        assertTrue(event.getTimestamp() > 0);
        assertEquals(ServerAction.Message, event.getAction());
        assertEquals(0, event.getParticipantCount());
        assertEquals("hello world", event.getMessage());
    }
}
