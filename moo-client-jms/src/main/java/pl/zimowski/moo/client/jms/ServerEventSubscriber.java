package pl.zimowski.moo.client.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.api.ServerEvent;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerEventSubscriber implements MessageListener {

    @Inject
    private Logger log;
    
    @Inject
    private JmsClientGateway jms;
    
    private ClientListener clientListener;


    void initialize(ClientListener clientListener) {
        
        this.clientListener = clientListener;

        try {
            jms.getServerEventsSubscriber().setMessageListener(this);
        }
        catch(JMSException e) {
            log.error("problem obtaining server events consumer", e);
            this.clientListener.onConnectToServerError(e.getMessage());
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
            clientListener.onEvent(serverEvent);
        }
    }
}
