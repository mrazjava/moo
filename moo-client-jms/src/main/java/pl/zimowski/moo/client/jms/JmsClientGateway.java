package pl.zimowski.moo.client.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.jms.JmsGateway;

/**
 * High level, JMS compliant access to client operations necessary to 
 * produce client events and consume server events.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class JmsClientGateway extends JmsGateway {

    @Inject
    private Logger log;
    
    private MessageProducer clientEventsProducer;
    
    private MessageConsumer serverEventsConsumer;
    
    private MessageConsumer serverEventsSubscriber;

    
    /**
     * Access to a producer for client generated events. This producer is used to 
     * send client events directly to a server. Created upon the very first 
     * invocation, then cached. Subsequent invocations do not create another instance.
     * 
     * @return producer to use for generating client events
     * @throws JMSException if producer could not be created for whatever reason
     */
    public MessageProducer getClientEventsProducer() throws JMSException {
        
        if(clientEventsProducer == null) {
            clientEventsProducer = session.createProducer(getClientEventQueueDestination());
            log.debug("created:\n{}", clientEventsProducer);
        }
        
        return clientEventsProducer;
    }

    /**
     * Access to consumer of server generated events. This consumer is used for P2P 
     * communication with the server (private messages). Created upon the very first 
     * invocation, then cached. Subsequent invocations do not create another instance.
     * 
     * @param clientId to use as a filter (selector) for direct messages
     * @return consumer to used for handling server events
     * @throws JMSException if consumer could not be created for whatever reason
     */
    public MessageConsumer getServerEventsConsumer(String clientId) throws JMSException {
        
        if(serverEventsConsumer == null) {
            serverEventsConsumer = session.createConsumer(
                    getServerEventQueueDestination(), 
                    String.format("clientId = '%s'", clientId)
                    );
            log.debug("created:\n{}", serverEventsConsumer);
        }
        
        return serverEventsConsumer;
    }
    
    /**
     * Access to subscriber of server generated events for public consumption (all 
     * clients). These events are mostly chat messages. Created upon the very first 
     * invocation, then cached. Subsequent invocations do not create another instance.
     * 
     * @return publisher to use for streaming public sever events
     * @throws JMSException if publisher could not be created for whatever reason
     */
    public MessageConsumer getServerEventsSubscriber() throws JMSException {
        
        if(serverEventsSubscriber == null) {
            serverEventsSubscriber = session.createConsumer(getServerEventTopicDestination());
            log.debug("created:\n{}", serverEventsSubscriber);
        }
        
        return serverEventsSubscriber;
    }

    /**
     * Given a client event, constructs JMS compliant message that can be sent to the 
     * broker.
     * 
     * @param event to be wrapped in a JMS message
     * @return JMS message containing the client event
     * @throws JMSException if message could not be created for whatever reason
     */
    public Message createClientEventMessage(ClientEvent event) throws JMSException {
        return session.createObjectMessage(event);
    }
}