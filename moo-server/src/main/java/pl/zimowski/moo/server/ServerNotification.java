package pl.zimowski.moo.server;

import pl.zimowski.moo.api.ClientEvent;

/**
 * Broadcasting of client events onto the server.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ServerNotification {

    /**
     * Given a thread associated with established (live) client, and specific
     * message it generated, tell server about it so it can distribute the
     * message to other clients.
     *
     * @param client which generated an event
     * @param event to be handled by the server
     * @return number of clients that were notified
     */
    int notify(ClientThread client, ClientEvent event);
}
