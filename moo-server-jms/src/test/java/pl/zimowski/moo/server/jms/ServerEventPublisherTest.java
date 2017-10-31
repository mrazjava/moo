package pl.zimowski.moo.server.jms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
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
 * Ensures that {@link ServerEventPublisher} operates as expected.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEventPublisherTest extends MooTest {

    @InjectMocks
    private ServerEventPublisher serverEventPublisher;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsServerGateway jms;


    @Test
    public void shouldPublish() throws JMSException {

        ServerEvent testEvent = new ServerEvent(ServerAction.Message);
        MessageProducer msgProducer = Mockito.mock(MessageProducer.class);

        when(jms.getServerEventsPublisher()).thenReturn(msgProducer);

        assertTrue(serverEventPublisher.publish(testEvent));
        verify(msgProducer, times(1)).send(null);
    }

    @Test
    public void shouldFailPublishOnJmsException() throws JMSException {

        when(jms.createServerEventMessage(Mockito.any())).thenThrow(JMSException.class);

        assertFalse(serverEventPublisher.publish(null));
        verify(jms, times(0)).getServerEventsPublisher();
    }
}
