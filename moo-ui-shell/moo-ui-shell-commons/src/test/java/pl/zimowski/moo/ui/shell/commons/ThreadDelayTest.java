package pl.zimowski.moo.ui.shell.commons;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.zimowski.moo.test.utils.MockLogger;

/**
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ThreadDelayTest {
    
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

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
