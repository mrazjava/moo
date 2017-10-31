package pl.zimowski.moo.client.jms;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.api.ClientReporting;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEventConsumerTest extends MooTest {

    @InjectMocks
    private ServerEventConsumer serverEventConsumer;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsClientGateway jms;

    @Mock
    private ClientReporting reporter;


    @Test
    public void shouldInitializeWithoutException() throws JMSException {

        String expectedUri = "localhost:9999";
        MessageConsumer msgConsumer = Mockito.mock(MessageConsumer.class);
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);

        ReflectionTestUtils.setField(serverEventConsumer, "brokerUrl", expectedUri);
        when(jms.getServerEventsConsumer(Mockito.anyString())).thenReturn(msgConsumer);

        serverEventConsumer.initialize(reporter, "foo-bar");

        verify(reporter).onBeforeServerConnect(uriCaptor.capture());
        assertEquals(expectedUri, uriCaptor.getValue());
    }

    @Test
    public void shouldInitializeAndHandleException() throws JMSException {

        when(jms.getServerEventsConsumer(Mockito.anyString())).thenThrow(JMSException.class);

        serverEventConsumer.initialize(reporter, "foo-bar");

        verify(reporter, Mockito.times(1)).onConnectToServerError(null);
    }

    @Test
    public void shouldProcessMessage() throws JMSException {

        ServerEvent testEvent = new ServerEvent(ServerAction.NickGenerated);
        ObjectMessage objectMessage = Mockito.mock(ObjectMessage.class);
        when(objectMessage.getObject()).thenReturn(testEvent);

        serverEventConsumer.onMessage(objectMessage);

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

        serverEventConsumer.onMessage(objectMessage);

    }
}