package pl.zimowski.moo.server.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ServerEvent;

/**
 * Given an already generated {@link ServerEvent}, ensures it is published so 
 * that registered subscribers can consume it. Events that are published are 
 * known as "public" event, that is events that are broadcasted (and seen) by 
 * every client. Most of those events are public chat activity.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerEventPublisher {

    @Inject
    private Logger log;
    
    @Inject
    private JmsServerGateway jms;
    
    
    public boolean publish(ServerEvent serverEvent) {

        boolean status = false;
        
        log.debug("publishing:\n{}", serverEvent);
        
        try {
            Message message = jms.createServerEventMessage(serverEvent);
            MessageProducer producer = jms.getServerEventsPublisher();
            producer.send(message);
            status = true;
        }
        catch (JMSException e) {
            log.error(e.getMessage());
        }
        
        return status;
    }
}
