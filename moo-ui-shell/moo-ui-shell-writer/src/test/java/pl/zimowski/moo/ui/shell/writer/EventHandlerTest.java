package pl.zimowski.moo.ui.shell.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Ensures that {@link EventHandler} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class EventHandlerTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @InjectMocks
    private EventHandler eventHandler;
    
    @Mock
    private ClientHandling clientHandler;
    
    /**
     * stubs connected state of a client handler
     */
    private boolean connected = true;

    
    @Test
    public void shouldReportCorrectAuthor() {
    	assertEquals(EventHandler.AUTHOR, eventHandler.getAuthor());
    }
    
    @Test
    public void shouldHandleConnectionEstablishedEvent() {
    	
    	String clientId = "foo-bar";
    	
    	ServerEvent event = new ServerEvent(ServerAction.ConnectionEstablished)
    			.withClientId(clientId);
    	
    	assertNull(eventHandler.getClientId());
    	eventHandler.onEvent(event);
    	assertEquals(clientId, eventHandler.getClientId());
    }
    
    @Test
    public void shouldHandleNickGeneratedEvent() {
    	
    	String nick = "johnie";
    	
    	ServerEvent event = new ServerEvent(ServerAction.NickGenerated)
    			.withMessage(nick);
    	
    	assertNull(eventHandler.getNick());
    	eventHandler.onEvent(event);
    	assertEquals(nick, eventHandler.getNick());
    }
    
    @Test
    public void shouldHandleServerExit() {
    	
    	ServerEvent event = new ServerEvent(ServerAction.ServerExit);
    	
    	Mockito.doAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock arg0) throws Throwable {
				return connected;
			}
    		
    	}).when(clientHandler).isConnected();
    	
    	Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock arg0) throws Throwable {
				connected = false;
				return null;
			}
    		
    	}).when(clientHandler).disconnect();
    	
    	
    	assertTrue(clientHandler.isConnected());
    	eventHandler.onEvent(event);
    	assertFalse(clientHandler.isConnected());
    }
    
    @Test
    public void shouldHandleOnBeforeServerConnect() {
    	
    	eventHandler.onBeforeServerConnect("localhost", 8000);
    }
    
    @Test
    public void shouldHandleOnConnectToServerError() {
    	
    	eventHandler.onConnectToServerError("foo bar");
    }
}
