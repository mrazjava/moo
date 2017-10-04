package pl.zimowski.moo.server.socket.jmx;

import static org.junit.Assert.assertEquals;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link JmxReporter} correctly initializes itself and 
 * that it correctly reports stats.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class JmxReporterTest extends MooTest {
    
    private static final Logger log = LoggerFactory.getLogger(JmxReporterTest.class);

    @InjectMocks
    private JmxReporter reporter;
    
    @Spy
    private MockLogger mockLog;
    
    @Spy
    private ClientAnalytics analytics;
    
    
    @Test
    public void shouldInitialize() {
        
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        int beforeInitCount = mbs.getMBeanCount();
        log.debug("default mbean count: {}", mbs.getMBeanCount());
        
        reporter.initialize();
        
        assertEquals(Integer.valueOf(beforeInitCount+1), mbs.getMBeanCount());
    }
    
    @Test
    public void shouldTrackConnectedClient() {
        
        assertEquals(0, analytics.getConnectedClientCount());
        reporter.clientConnected();
        assertEquals(1, analytics.getConnectedClientCount());
    }
    
    @Test
    public void shouldTrackDisconnectedClient() {
        
        assertEquals(0, analytics.getDisconnectedClientCount());
        reporter.clientDisconnected();
        assertEquals(1, analytics.getDisconnectedClientCount());
    }
}
