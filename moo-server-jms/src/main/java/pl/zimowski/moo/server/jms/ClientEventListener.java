package pl.zimowski.moo.server.jms;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.server.commons.EventManager;

/**
 * Listens for client events, upon processing generates server events which
 * are either sent P2P (back to client) or published publicly (to all
 * clients).
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ClientEventListener implements MessageListener {

    @Inject
    private Logger log;

    @Inject
    private JmsServerGateway jms;

    @Inject
    private ServerEventSender serverEventSender;

    @Inject
    private ServerEventPublisher serverEventPublisher;

    @Inject
    private EventManager eventManager;

    private Set<String> connectedClients = new HashSet<>();


    @PostConstruct
    void initialize() throws JMSException {

        jms.getClientEventsConsumer().setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {

        log.debug("received:\n{}", message);

        ClientEvent clientEvent = null;

        if(message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage)message;
            Object object = null;
            try {
                object = objectMessage.getObject();
                if(object instanceof ClientEvent) {
                    clientEvent = (ClientEvent)object;
                    log.debug("extracted:\n{}", clientEvent);
                }
                else {
                    log.warn("unexpected payload type:\n{}", object);
                }
            }
            catch (JMSException e) {
                log.warn("problem extracting payload: {}", e.getMessage());
            }
        }

        if(clientEvent != null) {
            ServerEvent serverEvent = processClientEvent(clientEvent);
            if(serverEvent != null) {
                ServerAction action = serverEvent.getAction();

                // detect public action and publish event
                if(action == ServerAction.ClientDisconnected ||
                        action == ServerAction.Message ||
                        action == ServerAction.ParticipantCount ||
                        action == ServerAction.ServerExit) {

                    serverEventPublisher.publish(serverEvent);
                }
            }
        }
    }

    /**
     * Given a client event, detects it nature and processes P2P first generating
     * one (or more) relavant P2P events if client event is private. If client
     * event is public, only simple translation (to server event) takes place and
     * no P2P submission takes place. Regardless, translated server event is returned
     * which should be handled by publisher is its action makes it a public event.
     *
     * @param clientEvent generated by some client
     * @return related server event
     */
    private ServerEvent processClientEvent(ClientEvent clientEvent) {

        String clientId = clientEvent.getClientId();
        ServerEvent serverEvent = null;

        if(clientEvent.getAction() == ClientAction.Connect) {

            serverEvent = new ServerEvent(ServerAction.ConnectionEstablished).withClientId(clientId);
            log.trace("CONNECTION ESTABLISHED\n{}", serverEvent);
            serverEventSender.send(
                    serverEvent,
                    clientId
                    );

            serverEvent = new ServerEvent(ServerAction.ParticipantCount).withMessage(
                    String.format("%d participant(s)", eventManager.getParticipantCount()));
            log.trace("PARTICIPANT COUNT\n{}", serverEvent);
            serverEventSender.send(
                    serverEvent,
                    clientId
                    );

            connectedClients.add(clientId);
        }

        if(clientEvent.getAction() == ClientAction.Disconnect) {
            connectedClients.remove(clientEvent.getClientId());
        }

        if(clientEvent.getAction() == ClientAction.Signin) {
            serverEvent = new ServerEvent(ServerAction.SigninConfirmed).withAuthor(clientEvent.getAuthor());
            log.trace("SIGNING CONFIRMED\n{}", serverEvent);
            serverEventSender.send(
                    serverEvent,
                    clientId
                    );
            serverEvent = null;
        }

        serverEvent = eventManager.clientEventToServerEvent(clientEvent);

        if(clientEvent.getAction() == ClientAction.GenerateNick) {
            log.trace("GENERATE NICK\n{}", serverEvent);
            serverEventSender.send(serverEvent, clientId);
            serverEvent = null;
        }

        return serverEvent;
    }
}