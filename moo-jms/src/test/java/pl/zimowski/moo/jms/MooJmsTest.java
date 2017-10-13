package pl.zimowski.moo.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that core {@link JmsGateway} operates as expected.
 * 
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class MooJmsTest extends MooTest {

    @InjectMocks
    private JmsGateway jms;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private Context jndi;
    
    @Mock
    private ConnectionFactory factory;
    
    @Mock
    private Connection connection;
    
    @Mock
    private Session session;
    
    @Mock
    private Destination clientEventQueueDestination;
    
    @Mock
    private Destination serverEventQueueDestination;
    
    @Mock
    private Destination serverEventTopicDestination;
    
    
    @Test
    public void shouldConnect() throws NamingException, JMSException {
        
        when(jndi.lookup(Mockito.anyString())).thenReturn(factory);
        when(factory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);

        jms.connect();
        Session session = jms.getSession();
        
        assertNotNull(session);
    }
    
    @Test(expected = JMSException.class)
    public void shouldNotConnect() throws NamingException, JMSException {
        
        when(jndi.lookup(Mockito.anyString())).thenReturn(factory);
        when(factory.createConnection()).thenReturn(connection);
        doThrow(JMSException.class).when(connection).start();
        
        jms.connect();
    }
    
    @Test
    public void shouldSucceedOnJndiLookup() throws NamingException {
        
        String foo = "foo";
        String bar = "bar";
        
        when(jndi.lookup(foo)).thenReturn(bar);
        
        Object result = jms.jndiLookup(foo);
        
        assertEquals(bar, result);
    }
    
    @Test
    public void shouldFailOnJndiLookup() throws NamingException {

        when(jndi.lookup(Mockito.anyString())).thenThrow(NamingException.class);
        
        assertNull(jms.jndiLookup("foo"));
    }
    
    @Test
    public void shouldGetClientEventQueueDestination() throws NamingException {
        
        when(jndi.lookup(Mockito.anyString())).thenReturn(clientEventQueueDestination);
        
        Destination destination = jms.getClientEventQueueDestination();
        assertEquals(clientEventQueueDestination, destination);
    }
    
    @Test
    public void shouldGetServerEventQueueDestination() throws NamingException {
        
        when(jndi.lookup(Mockito.anyString())).thenReturn(serverEventQueueDestination);
        
        Destination destination = jms.getServerEventQueueDestination();
        assertEquals(serverEventQueueDestination, destination);        
    }
    
    @Test
    public void shouldGetServerEventTopicDestination() throws NamingException {
        
        when(jndi.lookup(Mockito.anyString())).thenReturn(serverEventTopicDestination);
        
        Destination destination = jms.getServerEventTopicDestination();
        assertEquals(serverEventTopicDestination, destination);        
    }
    
    @Test
    public void shouldDisconnectGracefully() {
        jms.disconnect();
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldDisconnectCatchSessionCloseFailure() throws JMSException {
        
        // ensure that closing session results in exception (which should be handled), 
        // then mock session to throw another exception on connection close (which is 
        // not subject of this test
        
        doThrow(JMSException.class).when(session).close();
        doThrow(IllegalStateException.class).when(connection).close();
        
        jms.disconnect();
    }
    
    @Test
    public void shouldDisconnectCatchConnectionCloseFailure() throws JMSException {
        
        doThrow(JMSException.class).when(connection).close();
        
        jms.disconnect();
    }
}
