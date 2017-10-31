package pl.zimowski.moo.test.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Ensures that base for moo tests operates correctly.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class MooTestTest extends MooTest {

    @Test
    public void shouldSetLogSystemProperty() {

        assertEquals("target/logs-test", System.getProperty("LOG_DIR"));
    }
}
