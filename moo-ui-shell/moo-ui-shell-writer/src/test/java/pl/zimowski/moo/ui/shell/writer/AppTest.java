package pl.zimowski.moo.ui.shell.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.commons.MockLogger;
import pl.zimowski.moo.ui.shell.commons.ExecutionThrottling;
import pl.zimowski.moo.ui.shell.commons.ShutdownAgent;

/**
 * Ensures that {@link App} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class AppTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    
    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @InjectMocks
    private App writer;
    
    @Spy
    private MockLogger mockLog;

    @Spy
    private FakeClientHandler clientHandler;
    
    @Mock
    private EventHandler eventHandler;
    
    @Mock
    private ShutdownAgent shutdownAgent;
    
    @Mock
    private ExecutionThrottling throttler;
    
    
	/**
	 * Ensures that app can establish connection, produce user nick and 
	 * generate chat message before terminating.
	 */
	@Test
	public void shouldProduceChatWithoutNickAndExit() throws Exception {
	    
	    List<ClientEvent> events = produceChatAndExit(" ");
        
        assertTrue(events.size() == 5);
        
        assertEquals(ClientAction.GenerateNick, events.get(0).getAction());
        assertEquals(ClientAction.Signin, events.get(1).getAction());
        assertEquals(ClientAction.Message, events.get(2).getAction());
        assertEquals(ClientAction.Signoff, events.get(3).getAction());
        assertEquals(ClientAction.Disconnect, events.get(4).getAction());
	}
	
	@Test
	public void shouldProduceChatWithNickAndExit() throws Exception {
	    
	    List<ClientEvent> events = produceChatAndExit("misiu");
        
        assertTrue(events.size() == 4);
        
        assertEquals(ClientAction.Signin, events.get(0).getAction());
        assertEquals(ClientAction.Message, events.get(1).getAction());
        assertEquals(ClientAction.Signoff, events.get(2).getAction());
        assertEquals(ClientAction.Disconnect, events.get(3).getAction());
	}
	
	/**
	 * @param nick to use or empty string if one should be auto generated
	 * @return events produced by this setup
	 */
	private List<ClientEvent> produceChatAndExit(String nick) throws Exception {
	    
        when(eventHandler.getClientId()).thenReturn("foo-bar");
        when(eventHandler.getNick()).thenReturn("Rocky");
        
        systemInMock.provideLines(nick, "Howdy", "moo:exit");
        
        writer.run(null);
        writer.shutdown();
        
        return clientHandler.clientEvents;
	}
	
	@Test
	public void shouldNotConnect() throws Exception {
		
		when(clientHandler.connect(eventHandler)).thenReturn(false);
		
		writer.run(null);
		List<ClientEvent> events = clientHandler.clientEvents;
		assertTrue(events.isEmpty());
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrottleWhenMissingClientId() throws Exception {
	    
	    doThrow(IllegalStateException.class).when(throttler).throttle();
	    
	    writer.run(null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrottleWhenMissingNick() throws Exception {
	    
	    doThrow(IllegalStateException.class).when(throttler).throttle();
        when(eventHandler.getClientId()).thenReturn("foo-bar");
        
        systemInMock.provideLines(" ", "Howdy", "moo:exit");
        
        writer.run(null);
	}
	
	/**
	 * Handy mock useful for spying and verifying generated events.
	 * 
	 * @since 1.2.0
	 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
	 */
	class FakeClientHandler implements ClientHandling {
		
		List<ClientEvent> clientEvents = new ArrayList<>();

		@Override
		public boolean connect(ClientListener listener) {
			return true;
		}

		@Override
		public boolean isConnected() {
			return true;
		}

		@Override
		public void disconnect() {
		}

		@Override
		public void send(ClientEvent event) {
			clientEvents.add(event);
		}
	}
}
