package pl.zimowski.moo.server.jms;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.server.commons.EventManager;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientEventListener} correctly listens to 
 * JMS messages containing {@link ClientEvent}s.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ClientEventListenerTest extends MooTest {

    @InjectMocks
    private ClientEventListener listener;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private JmsServerGateway jms;
    
    @Mock
    private ServerEventSender serverEventSender;
    
    @Mock
    private ServerEventPublisher serverEventPublisher;
    
    @Mock
    private EventManager eventManager;
    
    
    @Test
    public void shouldInitialize() throws JMSException {
        
        MessageConsumer consumer = Mockito.mock(MessageConsumer.class);
        when(jms.getClientEventsConsumer()).thenReturn(consumer);
        
        listener.initialize();
        
        verify(consumer, times(1)).setMessageListener(listener);
    }
    
    @Test
    public void shouldProcessObjectMessage() throws JMSException {
        
        String clientId = "foo-bar";
        ClientEvent clientEvent = new ClientEvent(ClientAction.Disconnect).withClientId(clientId);
        ServerEvent serverEvent = new ServerEvent(ServerAction.ClientDisconnected).withClientId(clientId);
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenReturn(clientEvent);
        when(eventManager.clientEventToServerEvent(clientEvent)).thenReturn(serverEvent);
        
        listener.onMessage(message);
        
        verify(serverEventPublisher, times(1)).publish(serverEvent);
    }
    
    @Test
    public void shouldIgnoreTextMessage() {
        
        TextMessage message = Mockito.mock(TextMessage.class);
        listener.onMessage(message);
        verify(eventManager, times(0)).clientEventToServerEvent(Mockito.any());
    }
    
    @Test
    public void shouldRejectObjectMessageWrongType() throws JMSException {
        
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenReturn("not a ClientEvent");

        listener.onMessage(message);
        verify(eventManager, times(0)).clientEventToServerEvent(Mockito.any());
    }
    
    @Test
    public void shouldRejectMessageOnException() throws JMSException {
        
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenThrow(JMSException.class);
        
        listener.onMessage(message);
        verify(eventManager, times(0)).clientEventToServerEvent(Mockito.any());
    }
    
    @Test
    public void shouldHandleMessageWithNullServerMessage() throws JMSException {
        
        ClientEvent clientEvent = new ClientEvent(ClientAction.Connect);
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenReturn(clientEvent);
        
        listener.onMessage(message);
        
        verify(eventManager, times(1)).clientEventToServerEvent(clientEvent);
    }
    
    @Test
    public void shouldHandleMessageWithPrivateServerMessage() throws JMSException {
        
        ClientEvent clientEvent = new ClientEvent(ClientAction.Signin);
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenReturn(clientEvent);
        when(eventManager.clientEventToServerEvent(clientEvent)).thenReturn(new ServerEvent(ServerAction.SigninConfirmed));
        
        listener.onMessage(message);
        
        verify(eventManager, times(1)).clientEventToServerEvent(clientEvent);
    }
    
    @Test
    public void shouldHandleGenerateNickMessage() throws JMSException {
        
        ClientEvent clientEvent = new ClientEvent(ClientAction.GenerateNick);
        ObjectMessage message = Mockito.mock(ObjectMessage.class);
        when(message.getObject()).thenReturn(clientEvent);
        when(eventManager.clientEventToServerEvent(clientEvent)).thenReturn(new ServerEvent(ServerAction.NickGenerated).withMessage("rocky"));
        
        listener.onMessage(message);
        
        verify(eventManager, times(1)).clientEventToServerEvent(clientEvent);        
    }
}
