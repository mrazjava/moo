package pl.zimowski.moo.client.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientEvent;

/**
 * Submits client generated events to the JMS queue for the server to
 * pick up and process.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ClientEventSender {

    @Inject
    private Logger log;

    @Inject
    private JmsClientGateway jms;


    public boolean send(ClientEvent clientEvent) {

        boolean status = false;

        log.trace("processing:\n{}", clientEvent);

        try {
            Message message = jms.createClientEventMessage(clientEvent);
            MessageProducer producer = jms.getClientEventsProducer();
            producer.send(message);
            log.debug("message sent:\n{}", message);
            status = true;
        }
        catch (JMSException e) {
            log.error(e.getMessage());
        }

        return status;
    }
}
