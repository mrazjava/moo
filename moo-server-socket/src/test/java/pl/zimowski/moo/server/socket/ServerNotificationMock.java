package pl.zimowski.moo.server.socket;

import pl.zimowski.moo.api.ClientEvent;

/**
 * Useful mock which simulates server notifier and allows to test validity of
 * of {@link ClientThread} and {@link ClientEvent} to ensure notification
 * correctly reported them.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerNotificationMock implements EventBroadcasting {

    private ClientThread clientThread;

    private ClientEvent clientEvent;


    /**
     * {@inheritDoc} This implementation does not perform notification at all.
     * It simply caches parameters for later retrieval and always returns
     * zero to indicate that no clients were notified.
     */
    @Override
    public int broadcast(ClientThread client, ClientEvent event) {

        clientThread = client;
        clientEvent = event;
        return 0;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public ClientEvent getClientEvent() {
        return clientEvent;
    }
}
