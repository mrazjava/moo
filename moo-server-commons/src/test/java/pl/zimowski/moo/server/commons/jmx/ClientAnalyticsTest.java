package pl.zimowski.moo.server.commons.jmx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.zimowski.moo.server.commons.jmx.ClientAnalytics;

/**
 * Ensures that {@link ClientAnalytics} MBean correctly reports data.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ClientAnalyticsTest {

    @Test
    public void shouldReportCounts() {
        
        ClientAnalytics analytics = new ClientAnalytics();
        
        analytics.connectedClientCount = 22;
        analytics.disconnectedClientCount = 7;
        
        assertEquals(22, analytics.getConnectedClientCount());
        assertEquals(7, analytics.getDisconnectedClientCount());
    }
}
