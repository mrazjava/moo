package pl.zimowski.moo.server;

import pl.zimowski.moo.api.ServerEvent;

/**
 * Broadcasting of server events onto a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ClientNotification {

    /**
     * @return unique id of the client
     */
    String getClientId();

    /**
     * Given a message from server, inform a client about it. This operation
     * is intended to inform a specific client. If a server supports multiple
     * clients, it must notify each one separately.
     *
     * @param event emited by server to a client
     * @returns {@code true} if event was forwarded onto a client; {@code false} if not due to error
     */
    boolean notify(ServerEvent event);

    /**
     * @return snapshot of system clock (in millis) when last activity from
     *  the client was registered
     */
    long getLastActivity();

    /**
     * Terminate server's connection with the client such that subsequent
     * notifications are no longer possible.
     */
    void disconnect();
}
