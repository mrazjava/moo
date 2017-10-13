package pl.zimowski.moo.ui.shell.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ThreadDelayTest extends MooTest {

    @InjectMocks
    private ThreadDelay threadDelay;
    
    @Spy
    private MockLogger mockLog;
    
    
    @Test
    public void shouldThrotle() throws InterruptedException {

        threadDelay.delay = 10L;
        
        threadDelay.throttle();
        
        Thread testThread = new Thread() {

            @Override
            public void run() {
                threadDelay.throttle(2000L);
            }
        };

        testThread.start();
        testThread.interrupt();
    }
    
    @Test
    public void shouldTrackCount() {
        
        ReflectionTestUtils.setField(threadDelay, "count", 10);
        
        assertEquals(10, threadDelay.getCount());
        assertTrue(threadDelay.isCountExceeded(1));
        
        threadDelay.reset();
        
        assertEquals(0, threadDelay.getCount());
    }
}
