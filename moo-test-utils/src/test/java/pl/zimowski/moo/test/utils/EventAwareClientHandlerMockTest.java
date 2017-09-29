package pl.zimowski.moo.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;

/**
 * Ensures that {@link EventAwareClientHandlerMock} operates as expected.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class EventAwareClientHandlerMockTest {

    @Test
    public void shouldReturnCorrectValues() {
        
        List<ClientEvent> events = new ArrayList<>();
        EventAwareClientHandlerMock mock = new EventAwareClientHandlerMock();
        
        mock.setEventList(events);
        
        mock.send(new ClientEvent(ClientAction.Message));
        
        assertTrue(mock.connect(null));
        assertTrue(mock.isConnected());
        assertTrue(1 == events.size());
        assertEquals(ClientAction.Message, events.get(0).getAction());
    }
}
