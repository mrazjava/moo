package pl.zimowski.moo.server.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link EventManager} operates as expected.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class EventManagerTest extends MooTest {

    @InjectMocks
    private EventManager eventManager;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private ServerUtils serverUtils;
    
    private String clientId = "foo-bar";
    
    private String author = "rokita";
    
    
    @Test
    public void shouldHandleConnectEvent() {
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Connect).withClientId(clientId));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.ConnectionEstablished, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(0, serverEvent.getParticipantCount());
    }
    
    @Test
    public void shouldHandleSigninEventWith1Participant() {
        
        assertEquals(0, eventManager.getParticipantCount());
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Signin)
                    .withClientId(clientId).withAuthor(author));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.ParticipantCount, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(ServerEvent.AUTHOR, serverEvent.getAuthor());
        assertEquals(String.format("%s joined; and is the only participant so far", author), serverEvent.getMessage());
        assertEquals(1, eventManager.getParticipantCount());
    }
    
    @Test
    public void shouldHandleSigninEventWith2Participants() {
        
        ReflectionTestUtils.setField(eventManager, "participantCount", 1);
        
        assertEquals(1, eventManager.getParticipantCount());
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Signin)
                    .withClientId(clientId).withAuthor(author));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.ParticipantCount, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(ServerEvent.AUTHOR, serverEvent.getAuthor());
        assertEquals(String.format("%s joined; %d participants", author, 2), serverEvent.getMessage());
        assertEquals(2, eventManager.getParticipantCount());        
    }
    
    @Test
    public void shouldHandleSignoffEvent() {
        
        ReflectionTestUtils.setField(eventManager, "participantCount", 3);
        
        assertEquals(3, eventManager.getParticipantCount());
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Signoff)
                    .withClientId(clientId).withAuthor(author));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.ParticipantCount, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(ServerEvent.AUTHOR, serverEvent.getAuthor());
        assertEquals(String.format("%s left; %d participant(s) remaining", author, 2), serverEvent.getMessage());
        assertEquals(2, serverEvent.getParticipantCount());
    }
    
    @Test
    public void shouldHandleMessageEvent() {
        
        String message = "hello world!";
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Message).withClientId(clientId)
                    .withAuthor(author).withMessage(message));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.Message, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(author, serverEvent.getAuthor());
        assertEquals(message, serverEvent.getMessage());
    }
    
    @Test
    public void shouldHandleGenerateNickEvent() {
        
        String nick = "commando";
        
        when(serverUtils.randomNickName(0)).thenReturn(nick);
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.GenerateNick).withClientId(clientId));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.NickGenerated, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(ServerEvent.AUTHOR, serverEvent.getAuthor());
        assertEquals(nick, serverEvent.getMessage());
    }
    
    @Test
    public void shouldHandleDisconnectEvent() {
        
        ServerEvent serverEvent = eventManager.clientEventToServerEvent(
                new ClientEvent(ClientAction.Disconnect).withClientId(clientId));
        
        assertNotNull(serverEvent);
        assertEquals(ServerAction.ClientDisconnected, serverEvent.getAction());
        assertEquals(clientId, serverEvent.getClientId());
        assertEquals(ServerEvent.AUTHOR, serverEvent.getAuthor());
        assertEquals(String.format("client disconnect: %s", clientId), serverEvent.getMessage());
    }
}