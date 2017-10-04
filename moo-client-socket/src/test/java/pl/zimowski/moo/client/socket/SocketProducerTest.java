package pl.zimowski.moo.client.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link SocketProducer} correctly opens a socket.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class SocketProducerTest extends MooTest {

    private static final Logger log = LoggerFactory.getLogger(SocketProducerTest.class);
    
    @InjectMocks
    private SocketProducer producer;
    
    @Spy
    private MockLogger mockLog;

    
    @Test
    public void shouldGetSocket() throws IOException {
        
        String host = "127.0.0.1";
        int port = 8001;
        
        ServerSocket server = new ServerSocket(8001);

        ReflectionTestUtils.setField(producer, "host", host);
        ReflectionTestUtils.setField(producer, "port", port);
        
        assertNull(producer.getStatus());
        Socket socket = producer.getSocket();
        assertNotNull(socket);
        
        log.debug("{}", socket.getPort());
        
        assertEquals(host, producer.getSocketHost());
        assertEquals(port, producer.getSocketPort());
        assertEquals(host, socket.getInetAddress().getHostAddress());
        assertEquals(port, socket.getPort());
        assertFalse(socket.isClosed());
        assertEquals(String.format("connected to %s@%d", host, port), producer.getStatus());
        
        socket.close();
        server.close();
        
        assertTrue(socket.isClosed());        
    }
    
    @Test
    public void shouldNotGetSocketWithoutServer() throws IOException {
        
        String host = "localhost";
        int port = 8001;

        ReflectionTestUtils.setField(producer, "host", host);
        ReflectionTestUtils.setField(producer, "port", port);
        
        assertNull(producer.getStatus());
        assertNull(producer.getSocket());

        log.info(producer.getStatus());
        
        assertTrue(producer.getStatus().startsWith("SOCKET INIT FAILED:"));
    }
}
