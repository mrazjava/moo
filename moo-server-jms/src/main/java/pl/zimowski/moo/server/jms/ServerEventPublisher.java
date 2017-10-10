package pl.zimowski.moo.server.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.jms.JmsHandler;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerEventPublisher {

    @Inject
    private Logger log;
    
    @Inject
    private JmsHandler jms;
    
    
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
