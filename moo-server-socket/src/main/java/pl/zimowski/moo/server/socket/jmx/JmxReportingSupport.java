package pl.zimowski.moo.server.socket.jmx;

/**
 * Maintains stats needed for JMX reporting.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface JmxReportingSupport {

    /**
     * Registers MBeans with JMX.
     */
    void initialize();

    /**
     * Increments count of connected clients.
     */
    void clientConnected();

    /**
     * Decrements count of connected clients and increments count of
     * dicsonnected clients.
     */
    void clientDisconnected();
}
