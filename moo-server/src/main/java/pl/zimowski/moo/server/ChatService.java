package pl.zimowski.moo.server;

/**
 * High level operations that can be invoked on a chat service.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ChatService {

    /**
     * Starts chat service. After this operation, remote client should be able
     * to connect to a service.
     */
    void start();

    /**
     * Reports if service is up or down.
     *
     * @return {@code true} if service is running, {@code false} otherwise.
     */
    boolean isRunning();

    /**
     * Stop a running server. All connected clients will be disconnected by
     * force. After this operation no client should be able to establish a new
     * connection (unless {@link #start()} is subsequently invoked).
     */
    void stop();
}
