package pl.zimowski.moo.client.socket;

import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientListener;

/**
 * Ability to establish connection from a client to a server and do basic
 * operations on it.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ConnectionManagement {

    /**
     * Initiate connection from client to server. After this operation succeeds,
     * it should be possible to communicate with the server.
     *
     * @return {@code true} if connection was established; {@code false} if connection failed
     */
    boolean connect(ClientListener listener);

    void disconnect();

    boolean isConnected();

    /**
     * Notify server of an event that took place on a client.
     *
     * @param event to inform server about
     */
    void send(ClientEvent event);
}