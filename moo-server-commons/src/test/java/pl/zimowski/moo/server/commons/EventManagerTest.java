package pl.zimowski.moo.server.commons;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link EventManager} operates as expected.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class EventManagerTest extends MooTest {

    @InjectMocks
    private EventManager eventManager;
    
    @Mock
    private ServerUtils serverUtils;
    
    
    @Test
    public void shouldGenerateServerEvents() {
        
    }
}
