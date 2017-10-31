package pl.zimowski.moo.client.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.api.ServerEvent;

/**
 * P2P consumption of private server messages. Consumer is bound to a selector
 * of a specific client ID, so that only private server messages intended for
 * that client are consumed. Each received message is forwarded onto
 * {@link ClientReporting}. Private server messages are those that other clients
 * are not supposed to see, such as confirmation of established connection or
 * generation of random nick name.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ServerEventConsumer implements MessageListener {

    @Inject
    private Logger log;

    @Inject
    private JmsClientGateway jms;

    private ClientReporting reporter;

    @Value(value="${java.naming.provider.url}")
    private String brokerUrl;


    void initialize(ClientReporting reporter, String clientId) {

        this.reporter = reporter;
        this.reporter.onBeforeServerConnect(brokerUrl);

        try {
            jms.getServerEventsConsumer(clientId).setMessageListener(this);
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
