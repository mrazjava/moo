package pl.zimowski.moo.ui.shell.commons;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

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
}
