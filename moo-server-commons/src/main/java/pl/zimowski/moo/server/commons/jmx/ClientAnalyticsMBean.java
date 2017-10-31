package pl.zimowski.moo.server.commons.jmx;

/**
 * Client stats reportable over JMX.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ClientAnalyticsMBean {

    /**
     * @return number of clients currently connected to a server
     */
    long getConnectedClientCount();

    /**
     * @return number of clients that server handled and are no longer connected
     */
    long getDisconnectedClientCount();
}