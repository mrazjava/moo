package pl.zimowski.moo.server.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.jms.JmsGateway;

/**
 * High level, JMS compliant access to server operations necessary to 
 * co-ordinate chat events. 
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class JmsServerGateway extends JmsGateway {

    @Inject
    private Logger log;
    
    private MessageProducer serverEventsProducer;
    
    private MessageProducer serverEventsPublisher;
    
    private MessageConsumer clientEventsConsumer;
    
    
    /**
     * Access to a producer for server generated events. This producer is used to 
     * publish server events directly to a specific client. Created upon the very first 
     * invocation, then cached. Subsequent invocations do not create another instance.
     * 
     * @return producer to use for generating server events
     * @throws JMSException if producer could not be created for whatever reason
     */
    public MessageProducer getServerEventsProducer() throws JMSException {
        
        if(serverEventsProducer == null) {
            serverEventsProducer = session.createProducer(getServerEventQueueDestination());
            log.debug("created:\n{}", serverEventsProducer);
        }
        
        return serverEventsProducer;
    }
    
    /**
     * Access to consumer of client generated events. Created upon the very first 
     * invocation, then cached. Subsequent invocations do not create another instance.
     * 
     * @return consumer to used for handling client events
     * @throws JMSException if consumer could not be created for whatever reason
     */
    public MessageConsumer getClientEventsConsumer() throws JMSException {
        
        if(clientEventsConsumer == null) {
            clientEventsConsumer = session.createConsumer(getClientEventQueueDestination());
            log.debug("created:\n{}", clientEventsConsumer);
        }

        return clientEventsConsumer;
    }
    
    /**
     * Access to publisher of server generated events. Used to stream public server 
     * events meant for consumption by all clients. These events are mostly chat 
     * messages. Created upon the very first invocation, then cached. Subsequent 
     * invocations do not create another instance.
     * 
     * @return publisher to use for streaming public sever events
     * @throws JMSException if publisher could not be created for whatever reason
     */
    public MessageProducer getServerEventsPublisher() throws JMSException {
        
        if(serverEventsPublisher == null) {
            serverEventsPublisher = session.createProducer(getServerEventTopicDestination());
            log.debug("created:\n{}", serverEventsPublisher);
        }
        
        return serverEventsPublisher;
    }
    
    /**
     * Given a server event, constructs JMS compliant message that can be sent to the 
     * broker.
     * 
     * @param event to be wrapped in a JMS message
     * @return JMS message containing the server event
     * @throws JMSException if message could not be created for whatever reason
     */
    public Message createServerEventMessage(ServerEvent event) throws JMSException {
        return session.createObjectMessage(event);
    }
}