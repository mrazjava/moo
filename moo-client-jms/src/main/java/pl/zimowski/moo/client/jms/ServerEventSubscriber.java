package pl.zimowski.moo.client.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Listens for public server messages (intended for all clients) and relies them
 * over to {@link ClientReporting}.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ServerEventSubscriber implements MessageListener {

    @Inject
    private Logger log;

    @Inject
    private JmsClientGateway jms;

    private ClientReporting reporter;


    void initialize(ClientReporting reporter) {

        this.reporter = reporter;

        try {
            jms.getServerEventsSubscriber().setMessageListener(this);
        }
        catch(JMSException e) {
            log.error("problem obtaining server events consumer", e);
            this.reporter.onConnectToServerError(e.getMessage());
        }
    }

    @Override
    public void onMessage(Message message) {

        ObjectMessage objMessage = (ObjectMessage)message;
        ServerEvent serverEvent = null;

        try {
            serverEvent = (ServerEvent)objMessage.getObject();
        }
        catch (JMSException e) {
            log.error("problem retrieving {} message: {}", ServerEvent.class.getSimpleName(), e.getMessage());
        }

        if(serverEvent != null) {
            reporter.onEvent(serverEvent);
        }
    }
}
