package pl.zimowski.moo.ui.shell.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientReporter} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientReporterTest extends MooTest {

    @InjectMocks
    private ClientReporter reporter;

    @Mock
    private ClientHandling clientHandler;

    private boolean connected;


    @Test
    public void shouldEstablishConnection() {

        assertNull(reporter.getClientId());
        reporter.onEvent(new ServerEvent(ServerAction.ConnectionEstablished).withClientId("foo-bar"));
        assertEquals("foo-bar", reporter.getClientId());
    }

    @Test
    public void shouldReturnAuthor() {

        assertEquals(ClientReporter.AUTHOR, reporter.getAuthor());
    }

    @Test
    public void shouldRecordNick() {

        assertNull(reporter.getNick());
        reporter.onEvent(new ServerEvent(ServerAction.NickGenerated).withMessage("johnie"));
        assertEquals("johnie", reporter.getNick());
    }

    @Test
    public void shouldDisconnectOnExit() {

        connected = true;

        Mockito.doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock arg0) throws Throwable {
                connected = false;
                return null;
            }

        }).when(clientHandler).disconnect();

        reporter.onEvent(new ServerEvent(ServerAction.ServerExit));
        assertFalse(connected);
    }

    @Test
    public void shouldHandleMessage() {

        reporter.onEvent(new ServerEvent(ServerAction.Message).withMessage("hello"));

        // nothing to assert as message is simply logged
        // at least we covered extra branch :-)
    }
}