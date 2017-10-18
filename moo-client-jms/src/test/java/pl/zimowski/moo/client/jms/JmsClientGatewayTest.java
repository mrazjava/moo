package pl.zimowski.moo.client.jms;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class JmsClientGatewayTest extends MooTest {

    @InjectMocks
    private JmsClientGateway jms;

    @Spy
    private MockLogger mockLog;

    @Mock
    private Session session;

    @Mock
    private Context context;


    @Test
    public void shouldGetClientEventsProducer() throws JMSException {

        MessageProducer clientEventsProducer = Mockito.mock(MessageProducer.class);
        when(session.createProducer(Mockito.any())).thenReturn(clientEventsProducer);
        MessageProducer producer = jms.getClientEventsProducer();

        assertSame(clientEventsProducer, producer);
    }

    @Test
    public void shouldGetServerEventsConsumer() throws JMSException {

        String clientId = "foo-bar";
        MessageConsumer serverEventsConsumer = Mockito.mock(MessageConsumer.class);
        when(session.createConsumer(Mockito.any(), Mockito.eq("clientId = '" + clientId + "'"))).thenReturn(serverEventsConsumer);
        MessageConsumer consumer = jms.getServerEventsConsumer(clientId);

        assertSame(serverEventsConsumer, consumer);
    }

    @Test
    public void shouldGetServerEventsSubscriber() throws JMSException {

        MessageConsumer serverEventsSubscriber = Mockito.mock(MessageConsumer.class);
        when(session.createConsumer(Mockito.any())).thenReturn(serverEventsSubscriber);
        MessageConsumer subscriber = jms.getServerEventsSubscriber();

        assertSame(serverEventsSubscriber, subscriber);
    }

    @Test
    public void shouldCreateClientEventMessage() throws JMSException {

        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(session.createObjectMessage(Mockito.any())).thenReturn(message);

        assertSame(message, jms.createClientEventMessage(new ClientEvent(ClientAction.Message)));
    }
}
