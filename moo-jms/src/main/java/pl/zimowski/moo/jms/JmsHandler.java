package pl.zimowski.moo.jms;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerEvent;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class JmsHandler {
    
    /**
     * Destination for client generated events. There is only one consumer 
     * of those events - the JMS server. These events trigger the flow of 
     * JMS based chat service. As server receives an event from a client, 
     * it determines if the event is private or public. A private event, 
     * results in server responding directly to the client over {@link #QUEUE_SERVER_EVENTS}. 
     * An example of a private event action is {@link ClientAction#GenerateNick}. 
     * A public event is one that server broadcasts to all other clients 
     * over {@link #TOPIC_SERVER_EVENTS}. Example of public event action 
     * is {@link ClientAction#Message}.
     */
    public static final String QUEUE_CLIENT_EVENTS = "MooClientEvents";
    
    /**
     * Destination for server generated events. While there are many consumers 
     * of this queue, each server event on this queue is destined for one 
     * particular client. Each client (consumer of server events) initializes 
     * itself with a selector bound to unique client id. Consequently, each 
     * server event produced by the server carries a unique client id of a 
     * client for which the event is meant to be consumed.
     */
    public static final String QUEUE_SERVER_EVENTS = "MooPrivateServerEvents";
    
    /**
     * Destination for all public server generated events. These events are 
     * meant to be broadcasted to all connected clients. Consequently, every 
     * client will be a subscriber to this topic.
     */
    public static final String TOPIC_SERVER_EVENTS = "MooPublicServerEvents";
    
    @Inject
    private Logger log;
    
    private Connection connection;
    
    private Session session;
    
    private InitialContext jndi;
    
    private MessageProducer clientEventsProducer;
    
    private MessageProducer serverEventsProducer;
    
    private MessageProducer serverEventsPublisher;
    
    private MessageConsumer clientEventsConsumer;
    
    private MessageConsumer serverEventsConsumer;
    
    private MessageConsumer serverEventsSubscriber;
    

    @PostConstruct
    private void connect() throws NamingException {
        
        jndi = new InitialContext();
        ConnectionFactory conFactory = (ConnectionFactory)jndi.lookup("connectionFactory");
        
        try {
            connection = conFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
        }
        catch(JMSException e) {
            log.error("could not establish connection with JMS broker (is broker running?) - aborting! (ROOT CAUSE: {})", e.getMessage());
            System.exit(0);
        }
        
        log.debug("ready!");
    }
    
    public Session getSession() {
        return session;
    }
    
    public MessageProducer getClientEventsProducer() throws JMSException {
        
        if(clientEventsProducer == null) {
            //Queue queue = session.createQueue(QUEUE_CLIENT_EVENTS);
            clientEventsProducer = session.createProducer(getClientEventQueueDestination());
            log.debug("created:\n{}", clientEventsProducer);
        }
        
        return clientEventsProducer;
    }
    
    public MessageProducer getServerEventsProducer() throws JMSException {
        
        if(serverEventsProducer == null) {
            serverEventsProducer = session.createProducer(getServerEventQueueDestination());
            log.debug("created:\n{}", serverEventsProducer);
        }
        
        return serverEventsProducer;
    }
    
    public MessageConsumer getClientEventsConsumer() throws JMSException {
        
        if(clientEventsConsumer == null) {
            //Queue queue = session.createQueue(QUEUE_CLIENT_EVENTS);
            clientEventsConsumer = session.createConsumer(getClientEventQueueDestination());
            log.debug("created:\n{}", clientEventsConsumer);
        }

        return clientEventsConsumer;
    }

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
    
    public MessageProducer getServerEventsPublisher() throws JMSException {
        
        if(serverEventsPublisher == null) {
            serverEventsPublisher = session.createProducer(getServerEventTopicDestination());
            log.debug("created:\n{}", serverEventsPublisher);
        }
        
        return serverEventsPublisher;
    }
    
    public MessageConsumer getServerEventsSubscriber() throws JMSException {
        
        if(serverEventsSubscriber == null) {
            serverEventsSubscriber = session.createConsumer(getServerEventTopicDestination());
            log.debug("created:\n{}", serverEventsSubscriber);
        }
        
        return serverEventsSubscriber;
    }

    public Message createClientEventMessage(ClientEvent event) throws JMSException {
        return session.createObjectMessage(event);
    }
    
    public Message createServerEventMessage(ServerEvent event) throws JMSException {
        return session.createObjectMessage(event);
    }
    
    private Destination getClientEventQueueDestination() {
        return (Destination)jndiLookup(QUEUE_CLIENT_EVENTS);
    }
    
    private Destination getServerEventQueueDestination() {
        return (Destination)jndiLookup(QUEUE_SERVER_EVENTS);
    }
    
    private Destination getServerEventTopicDestination() {
        return (Destination)jndiLookup(TOPIC_SERVER_EVENTS);
    }
    
    public Object jndiLookup(String name) {
        
        Object object = null;
        
        try {
            object = jndi.lookup(name);
        }
        catch(NamingException e) {
            log.error("jndi lookup problem: {}", e.getMessage());
        }
        
        return object;
    }
    
    @PreDestroy
    private void disconnect() {
        stop();
    }
    
    public void stop() {
        
        try { 
            session.close();
        }
        catch(JMSException e) {
            log.info("session close: {}", e.getMessage());
        }
        
        try {
            connection.close();
        }
        catch(JMSException e) {
            log.info("connection close: {}", e.getMessage());
        }
    }
}
