package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        assertEquals(ServerAction.ParticipantCount, ServerAction.valueOf("ParticipantCount"));
        assertEquals(0, event.getParticipantCount());
        assertNull(event.getMessage());

        event.setMessage("foo");
        event.setNote("bar");

        assertEquals("foo", event.getMessage());
        assertEquals("bar", event.getNote());
    }

    @Test
    public void shouldProduceEventVia2ArgConstructor() {

        ServerEvent event = new ServerEvent(ServerAction.Message, "hello world");

        assertTrue(event.getTimestamp() > 0);
        assertEquals(ServerAction.Message, event.getAction());
        assertEquals(0, event.getParticipantCount());
        assertEquals("hello world", event.getMessage());
    }
    
    @Test
    public void shouldProduceEventWithFluidSetters() {
        
        ServerEvent event = new ServerEvent(ServerAction.Message)
                .withAuthor("johnie")
                .withClientId("foo-bar")
                .withMessage("howdy")
                .withNote("what?")
                .withParticipantCount(27);
        
        assertNotNull(event.getDateTime());
        assertEquals("johnie", event.getAuthor());
        assertEquals("foo-bar", event.getClientId());
        assertEquals("howdy", event.getMessage());
        assertEquals("what?", event.getNote());
        assertEquals(27, event.getParticipantCount());
    }
}