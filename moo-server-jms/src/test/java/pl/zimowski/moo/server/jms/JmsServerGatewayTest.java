package pl.zimowski.moo.server.jms;

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

import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link JmsServerGateway} operates as expected.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class JmsServerGatewayTest extends MooTest {

    @InjectMocks
    private JmsServerGateway jms;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private Session session;
    
    @Mock
    private Context context;
    
    
    @Test
    public void shouldGetServerEventsProducer() throws JMSException {
        
        MessageProducer serverEventsProducer = Mockito.mock(MessageProducer.class);
        when(session.createProducer(Mockito.any())).thenReturn(serverEventsProducer);
        MessageProducer producer = jms.getServerEventsProducer();
        
        assertSame(serverEventsProducer, producer);
    }
    
    @Test
    public void shouldGetClientEventsConsumer() throws JMSException {
        
        MessageConsumer clientEventsConsumer = Mockito.mock(MessageConsumer.class);
        when(session.createConsumer(Mockito.any())).thenReturn(clientEventsConsumer);
        MessageConsumer consumer = jms.getClientEventsConsumer();
        
        assertSame(clientEventsConsumer, consumer);
    }
    
    @Test
    public void shouldGetServerEventsPublisher() throws JMSException {
        
        MessageProducer serverEventsPublisher = Mockito.mock(MessageProducer.class);
        when(session.createProducer(Mockito.any())).thenReturn(serverEventsPublisher);
        MessageProducer publisher = jms.getServerEventsPublisher();
        
        assertSame(serverEventsPublisher, publisher);
    }

    @Test
    public void shouldCreateServerEventMessage() throws JMSException {
        
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(session.createObjectMessage(Mockito.any())).thenReturn(message);

        assertSame(message, jms.createServerEventMessage(new ServerEvent(ServerAction.ConnectionEstablished)));
    }
}