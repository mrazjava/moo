package pl.zimowski.moo.server.socket.jmx;

/**
 * Reports interesting stats about client connections to JMX console.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientAnalytics implements ClientAnalyticsMBean {

    long connectedClientCount;

    long disconnectedClientCount;


    @Override
    public long getConnectedClientCount() {
        return connectedClientCount;
    }

    @Override
    public long getDisconnectedClientCount() {
        return disconnectedClientCount;
    }
}