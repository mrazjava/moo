package pl.zimowski.moo.server;

import pl.zimowski.moo.api.ClientEvent;

/**
 * Useful mock which simulates server notifier and allows to test validity of
 * of {@link ClientThread} and {@link ClientEvent} to ensure notification
 * correctly reported them.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerNotificationMock implements ServerNotification {

    private ClientThread clientThread;

    private ClientEvent clientEvent;


    @Override
    public void notify(ClientThread client, ClientEvent event) {

        clientThread = client;
        clientEvent = event;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public ClientEvent getClientEvent() {
        return clientEvent;
    }
}
