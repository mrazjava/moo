package pl.zimowski.moo.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Ensures that {@link ChatEngine} operates as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ChatEngineTest {

    @Test
    public void shouldBeRunningAfterStart() {

        ChatService engine = new ChatEngine();

        assertFalse(engine.isRunning());
        engine.start();
        assertTrue(engine.isRunning());
    }
}
