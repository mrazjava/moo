package pl.zimowski.moo.server.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.server.socket.jmx.JmxReporter;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link WebSocketChatService} operates as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class WebSocketChatServiceTest extends MooTest {

    static final int TEST_PORT = 8001;

    @InjectMocks
    private WebSocketChatService chatService;

    @Spy
    private MockLogger mockLog;
    
    @Mock
    private ClientThread clientThread;
    
    @Mock
    private ServerUtils serverUtils;

    @Mock
    private ServerSocket serverSocket;
    
    @Mock
    private ServerSocketFactory serverSocketFactory;
    
    @Mock
    private Socket socket;
    
    @Mock
    private JmxReporter jmxReporter;
    
    @Mock
    private ClientNotification clientNotification;
    
    @Mock
    private ByteArrayOutputStream byteArrayOutputStream;
    
    private SocketAcceptAnswer socketAcceptAnswer;
    

    @Before
    public void setupServer() throws IOException {
        
        Mockito.when(serverSocket.accept()).thenAnswer(socketAcceptAnswer = new SocketAcceptAnswer());
        BDDMockito.given(serverSocketFactory.getServerSocket(TEST_PORT)).willReturn(serverSocket);
        BDDMockito.given(socket.getOutputStream()).willReturn(byteArrayOutputStream);
        
        chatService.setPort(TEST_PORT);
    }
    
    @Test
    public void shouldAcceptClientAndBroadcast() {

        
        assertEquals(0, chatService.getConnectedClientCount());

        chatService.start();

        assertEquals(1, chatService.getConnectedClientCount());
        assertEquals(1, chatService.broadcast(clientThread, new ClientEvent(ClientAction.Signin)));
        assertEquals(1, chatService.broadcast(clientThread, new ClientEvent(ClientAction.Message)));
        assertEquals(1,  chatService.broadcast(clientThread, new ClientEvent(ClientAction.GenerateNick)));
        assertEquals(1, chatService.broadcast(clientThread, new ClientEvent(ClientAction.Signoff)));
        assertEquals(1, chatService.broadcast(clientThread, new ClientEvent(ClientAction.Disconnect)));
        
        chatService.stop();
        
        assertFalse(chatService.isRunning());
        
        ArgumentCaptor<ServerEvent> serverEventsCaptor = ArgumentCaptor.forClass(ServerEvent.class);
        Mockito.verify(clientThread, Mockito.times(6)).notify(serverEventsCaptor.capture());

        List<ServerEvent> serverEvents = serverEventsCaptor.getAllValues();
        
        assertEquals(6, serverEvents.size());
        
        assertEquals(ServerAction.SigninConfirmed, serverEvents.get(0).getAction());
        assertEquals(ServerAction.ParticipantCount, serverEvents.get(1).getAction());
        assertEquals(ServerAction.Message, serverEvents.get(2).getAction());
        assertEquals(ServerAction.NickGenerated, serverEvents.get(3).getAction());
        assertEquals(ServerAction.ParticipantCount, serverEvents.get(4).getAction());
        assertEquals(ServerAction.ClientDisconnected, serverEvents.get(5).getAction());
        
        chatService.shutdown();
    }
    
    @Test
    public void shouldEvictInactiveClient() {
        
        ReflectionTestUtils.setField(chatService, "evictionTimeout", 0);
        chatService.start();
        
        assertEquals(1, chatService.getConnectedClientCount());
        assertEquals(0, chatService.broadcast(clientThread, new ClientEvent(ClientAction.Message)));
        assertEquals(0, chatService.getConnectedClientCount());
    }
    
    @Test
    public void shouldStartAndHandleIOException() throws IOException {
        
        Mockito.ignoreStubs(serverSocket.accept(), socket.getOutputStream());
        Mockito.when(serverSocketFactory.getServerSocket(TEST_PORT)).thenThrow(IOException.class);
        chatService.start();
    }
    
    @Test
    public void shouldListenAndHandleIOException() throws IOException {
        
        socketAcceptAnswer.throwable = new IOException();
        
        chatService.start();
        chatService.stop();
    }
    
    
    /**
     * @since 1.2.0
     * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
     */
    class SocketAcceptAnswer implements Answer<Socket>  {

        Throwable throwable;
        
        @Override
        public Socket answer(InvocationOnMock invocation) throws Throwable {
            ReflectionTestUtils.setField(chatService, "running", false);
            if(throwable != null) throw throwable;
            return socket;
        }
    }
}