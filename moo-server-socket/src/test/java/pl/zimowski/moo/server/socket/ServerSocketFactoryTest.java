package pl.zimowski.moo.server.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static pl.zimowski.moo.server.socket.WebSocketChatServiceTest.TEST_PORT;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Test;

/**
 * Ensures that {@link ServerSocketFactory} correctly produces {@link ServerSocket}.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class ServerSocketFactoryTest {

    @Test
    public void shouldCreateServerSocket() throws IOException {
        
        ServerSocket serverSocket = new ServerSocketFactory().getServerSocket(TEST_PORT);
        
        assertNotNull(serverSocket);
        assertEquals(TEST_PORT, serverSocket.getLocalPort());
        assertFalse(serverSocket.isClosed());
        
        serverSocket.close();
        
        assertTrue(serverSocket.isClosed());
        
    }
}
