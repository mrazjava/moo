package pl.zimowski.moo.client.jms;

import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.jms.JmsGateway;

/**
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
    private ClientEventSender clientEventProducer;
    
    @Inject
    private ServerEventConsumer serverEventConsumer;
    
    @Inject
    private ServerEventSubscriber serverEventSubscriber;
    
    private boolean connected;
    
    private String clientId;
    
    
    @Override
    public boolean connect(ClientListener listener) {

        clientId = UUID.randomUUID().toString();
        
        serverEventConsumer.initialize(listener, clientId);
        serverEventSubscriber.initialize(listener);
        
        ClientEvent connectEvent = new ClientEvent(ClientAction.Connect).withId(clientId);
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
        clientEventProducer.send(event);
    }
}