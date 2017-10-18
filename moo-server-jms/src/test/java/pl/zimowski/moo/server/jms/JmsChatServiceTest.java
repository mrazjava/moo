package pl.zimowski.moo.server.jms;

import static org.junit.Assert.assertFalse;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.mockito.Mockito.doThrow;

import javax.jms.JMSException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.jms.JmsGateway;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link JmsChatService} operates as expected.
 *
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class JmsChatServiceTest extends MooTest {

    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    @InjectMocks
    private JmsChatService chat;

    @Spy
    private MockLogger mockLog;

    @Mock
    private JmsGateway jms;


    @Test
    public void shouldStartAndExit() {

        systemInMock.provideLines("hello", "moo:exit");
        chat.start();
        assertFalse(chat.isRunning());
    }

    @Test
    public void shouldStartAndAbortOnJmsException() {

        doThrow(JMSException.class).when(jms).stop();

        systemInMock.provideLines("moo:exit");
        chat.start();
        assertFalse(chat.isRunning());
    }

    @Test
    public void shouldStop() {

        ReflectionTestUtils.setField(chat, "running", true);
        chat.stop();
        assertFalse(chat.isRunning());
    }
}
