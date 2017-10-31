package pl.zimowski.moo.client.jms;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.quality.Strictness;

import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ServerEventSubscriber} operates as expected.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEventSubscriberTest extends MooTest {

    @InjectMocks
    private ServerEventSubscriber serverEventSubscriber;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsClientGateway jms;

    @Mock
    private ClientReporting reporter;


    @Test
    public void shouldInitializeWithoutException() throws JMSException {

        MessageConsumer msgSubscriber = Mockito.mock(MessageConsumer.class);
        when(jms.getServerEventsSubscriber()).thenReturn(msgSubscriber);

        serverEventSubscriber.initialize(null);

        verify(msgSubscriber, times(1)).setMessageListener(serverEventSubscriber);
    }

    @Test
    public void shouldInitializeAndHandleException() throws JMSException {

        when(jms.getServerEventsSubscriber()).thenThrow(JMSException.class);

        serverEventSubscriber.initialize(reporter);

        verify(reporter, times(1)).onConnectToServerError(null);
    }

    @Test
    public void shouldProcessMessage() throws JMSException {

        ServerEvent testEvent = new ServerEvent(ServerAction.ParticipantCount);
        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenReturn(testEvent);

        serverEventSubscriber.onMessage(objectMessage);

        verify(reporter, times(1)).onEvent(testEvent);
    }

    @Test
    public void shouldHandleExceptionOnMessage() throws JMSException {


        mockito.strictness(Strictness.LENIENT);

        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenThrow(JMSException.class);

        // mockito is set to lienient mode so it won't complain when this
        // stub is not used; that's the point of this test, if this stub
        // ends up being used, exception will be thrown causing the test
        // to fail - under expected behavior, this stub should never be
        // invoked
        doThrow(IllegalStateException.class).when(reporter).onEvent(null);

        serverEventSubscriber.onMessage(objectMessage);
    }
}
