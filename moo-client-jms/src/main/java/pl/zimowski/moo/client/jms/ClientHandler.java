package pl.zimowski.moo.client.jms;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.jms.JmsGateway;

/**
 * Handles interaction with JMS compatible server.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ClientHandler implements ClientHandling {

    @Inject
    private Logger log;

    @Inject
    private JmsGateway jms;

    @Inject
    private ClientEventSender clientEventSender;

    @Inject
    private ServerEventConsumer serverEventConsumer;

    @Inject
    private ServerEventSubscriber serverEventSubscriber;

    private boolean connected;

    private String clientId;


    @Override
    public boolean connect(ClientReporting reporter) {

        clientId = UUID.randomUUID().toString();

        serverEventConsumer.initialize(reporter, clientId);
        serverEventSubscriber.initialize(reporter);

        ClientEvent connectEvent = new ClientEvent(ClientAction.Connect).withClientId(clientId);
        log.debug("connecting....\n{}", connectEvent);

        send(connectEvent);

        return connected = true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void disconnect() {
        jms.stop();
        connected = false;
    }

    @Override
    public void send(ClientEvent event) {
        log.debug("producing:\n{}", event);
        clientEventSender.send(event);
    }
}