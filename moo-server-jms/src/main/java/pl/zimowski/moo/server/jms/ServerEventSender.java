package pl.zimowski.moo.server.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ServerEvent;

/**
 * Given an already generated {@link ServerEvent} broadcasts it with specific  
 * client id as the message is meant for consumption by only one client. A 
 * client is expected to have registered itself as a consumer with a 
 * compatible client id selector.
 * <p>
 * These types of messages are known as "private" events which are intended 
 * for only one client. Example of such messages are confirmed connection, 
 * or generation of nick name.
 * </p>
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerEventSender {

    @Inject
    private Logger log;
    
    @Inject
    private JmsServerGateway jms;
    
    
    public boolean send(ServerEvent serverEvent, String clientId) {
        
        boolean status = false;
        
        log.debug("sending to client: {}\n{}", clientId, serverEvent);
        
        try {
            Message message = jms.createServerEventMessage(serverEvent);
            message.setStringProperty("clientId", clientId);
            MessageProducer producer = jms.getServerEventsProducer();
            producer.send(message);
            status = true;
        }
        catch (JMSException e) {
            log.error(e.getMessage());
        }
        
        return status;
    }
}
