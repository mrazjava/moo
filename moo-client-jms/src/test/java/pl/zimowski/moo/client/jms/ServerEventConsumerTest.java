package pl.zimowski.moo.client.jms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.quality.Strictness;

import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ServerEventConsumerTest extends MooTest {

    @InjectMocks
    private ServerEventConsumer serverEventConsumer;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private JmsClientGateway jms;
    
    @Mock
    private ClientListener clientListener;
    
    
    @Test
    public void shouldInitializeWithoutException() throws JMSException {
        
        MessageConsumer msgConsumer = Mockito.mock(MessageConsumer.class);
        ArgumentCaptor<String> hostCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> portCaptor = ArgumentCaptor.forClass(Integer.class);
        
        when(jms.getServerEventsConsumer(Mockito.anyString())).thenReturn(msgConsumer);
        
        serverEventConsumer.initialize(clientListener, "foo-bar");
        
        verify(clientListener).onBeforeServerConnect(hostCaptor.capture(), portCaptor.capture());
        
        String host = hostCaptor.getValue();
        int port = portCaptor.getValue();
        
        assertEquals("127.0.0.1", host);
        assertEquals(61616, port);
    }
    
    @Test
    public void shouldInitializeAndHandleException() throws JMSException {
        
        when(jms.getServerEventsConsumer(Mockito.anyString())).thenThrow(JMSException.class);
        
        serverEventConsumer.initialize(clientListener, "foo-bar");
        
        verify(clientListener, Mockito.times(1)).onConnectToServerError(null);
    }
    
    @Test
    public void shouldProcessMessage() throws JMSException {
        
        ServerEvent testEvent = new ServerEvent(ServerAction.NickGenerated);
        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenReturn(testEvent);
        
        serverEventConsumer.onMessage(objectMessage);
        
        verify(clientListener, times(1)).onEvent(testEvent);
    }
    
    @Test
    public void shouldHandleExceptionOnMessage() throws JMSException {
        
        mockito.strictness(Strictness.LENIENT);
        
        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenThrow(JMSException.class);
        
        // mockito is set to lienient mode so it won't complain when this 
        // stub is not used; that's the point of this test, if this stub 
        // ends up being used, exception will be thrown causing the test 
        // to fail - under expected behavior, this stub should never be 
        // invoked
        doThrow(IllegalStateException.class).when(clientListener).onEvent(null);        
        
        serverEventConsumer.onMessage(objectMessage);
        
    }
}