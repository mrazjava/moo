package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
        assertEquals(ClientAction.Signin, ClientAction.valueOf("Signin"));
        assertNull(event.getAuthor());
        assertNull(event.getMessage());
        assertNull(event.getClientId());

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
    
    @Test
    public void shouldEqual() {
        
        ClientEvent event1 = new ClientEvent(ClientAction.Connect);
        ClientEvent event2 = new ClientEvent(ClientAction.Connect);
        
        assertEquals(event1, event2);
        
        event1.withClientId("foobar123");
        event2.withClientId("foobar123");
        
        assertEquals(event1, event2);
        
        event1.withAuthor("bebe");
        event2.withAuthor("bebe");
        
        assertEquals(event1, event2);
        
        event1.withMessage("kontra");
        event2.withMessage("kontra");
        
        assertEquals(event1, event2);
        
        ReflectionTestUtils.setField(event1, "timestamp", 345L);
        ReflectionTestUtils.setField(event2, "timestamp", 345L);
        
        assertEquals(event1, event2);
    }
    
    @Test
    public void shouldNotEqual() {
        
        ClientEvent event1 = new ClientEvent(ClientAction.Connect);
        ClientEvent event2 = new ClientEvent(ClientAction.Disconnect);
        
        assertFalse(event1.equals(event2));
        assertFalse(event1.equals(null));
        assertFalse(event1.equals("event1"));
    }
}