package pl.zimowski.moo.client.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientEventSender} operates as expected.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ClientEventSenderTest extends MooTest {

    @InjectMocks
    private ClientEventSender sender;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private JmsClientGateway jms;
    
    @Mock
    private Message message;
    
    @Mock
    private MessageProducer msgProducer;
    
    
    @Test
    public void shouldSendEvent() throws JMSException {
        
        ClientEvent testEvent = new ClientEvent(ClientAction.Signin).withAuthor("pacanek");
        ArgumentCaptor<ClientEvent> clientEventCaptor = ArgumentCaptor.forClass(ClientEvent.class);

        when(jms.createClientEventMessage(Mockito.any())).thenReturn(message);
        when(jms.getClientEventsProducer()).thenReturn(msgProducer);
        
        assertTrue(sender.send(testEvent));

        verify(jms).createClientEventMessage(clientEventCaptor.capture());
        ClientEvent capturedEvent = clientEventCaptor.getValue();
        
        assertEquals(testEvent, capturedEvent);
    }
    
    @Test
    public void shouldHandleJmsException() throws JMSException {
        
        when(jms.createClientEventMessage(Mockito.any())).thenThrow(JMSException.class);
        assertFalse(sender.send(new ClientEvent(ClientAction.GenerateNick)));
    }
}
