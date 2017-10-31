package pl.zimowski.moo.server.jms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ServerEventSender} operates as expected.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEventSenderTest extends MooTest {

    @InjectMocks
    private ServerEventSender serverEventSender;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsServerGateway jms;


    @Test
    public void shouldSend() throws JMSException {

        String clientId = "foo-bar";
        ServerEvent testEvent = new ServerEvent(ServerAction.NickGenerated);
        Message msg = Mockito.mock(Message.class);
        MessageProducer msgProducer = Mockito.mock(MessageProducer.class);

        when(jms.createServerEventMessage(testEvent)).thenReturn(msg);
        when(jms.getServerEventsProducer()).thenReturn(msgProducer);

        assertTrue(serverEventSender.send(testEvent, clientId));
        verify(msg, times(1)).setStringProperty("clientId", clientId);
        verify(msgProducer, times(1)).send(msg);
    }

    @Test
    public void shouldFailSendOnJmsException() throws JMSException {

        when(jms.createServerEventMessage(Mockito.any())).thenThrow(JMSException.class);

        assertFalse(serverEventSender.send(null, null));
        verify(jms, times(0)).getServerEventsProducer();
    }
}
