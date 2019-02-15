package pl.zimowski.moo.ui.shell.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.quality.Strictness;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.commons.ShutdownAgent;
import pl.zimowski.moo.test.utils.EventAwareClientHandlerMock;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;
import pl.zimowski.moo.ui.shell.commons.ExecutionThrottling;

/**
 * Ensures that {@link App} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class AppTest extends MooTest {

    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @InjectMocks
    private App reader;

    @Spy
    private MockLogger mockLog;

    @Spy
    private EventAwareClientHandlerMock clientHandler;

    @Mock
    private ClientReporter eventReporter;

    @Mock
    private ShutdownAgent shutdownAgent;

    @Mock
    private ExecutionThrottling throttler;


    @Test
    public void shouldNotConnect() throws Exception {

        List<ClientEvent> eventList = new ArrayList<>();

        clientHandler.setEventList(eventList);
        when(clientHandler.connect(eventReporter)).thenReturn(false);

        reader.run(null);

        assertTrue(eventList.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldConnectButThrottleOnNullClientId() throws Exception {

        doThrow(IllegalStateException.class).when(throttler).throttle();

        reader.run(null);
    }

    @Test
    public void shouldConnectButShutdownOnThrottleMaxLimit() throws Exception {

        when(throttler.isCountExceeded(null)).thenReturn(true);

        reader.run(null);
    }

    /**
     * Very similar setup to {@link #shouldConnectButThrottleOnNullClientId()}
     * as throttler is configured to throw exceptione very time, however,
     * because reporter is also configured to return client id, throttler
     * will never throttle and so exception should never be thrown.
     *
     * @throws Exception whenever {@link App#run(org.springframework.boot.ApplicationArguments)} does
     */
    @Test
    public void shouldConnectAndExitWithoutException() throws Exception {

        mockito.strictness(Strictness.LENIENT);

        doThrow(IllegalStateException.class).when(throttler).throttle();
        when(eventReporter.getClientId()).thenReturn("foo-bar");

        systemInMock.provideLines("moo:exit");

        reader.run(null);
    }

    @Test
    public void shouldShutdownAndDisconnect() {

        List<ClientEvent> events = new ArrayList<>();

        when(eventReporter.newDisconnectEvent()).thenReturn(new ClientEvent(ClientAction.Disconnect));

        clientHandler.setEventList(events);
        assertTrue(events.isEmpty());
        reader.shutdown();

        assertEquals(1, events.size());
        assertEquals(ClientAction.Disconnect, events.get(0).getAction());
    }
}
