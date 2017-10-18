package pl.zimowski.moo.client.socket.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Ensures opration of {@link ServerSettings}.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerSettingsTest {

    @Test
    public void shouldAcceptConfiguration() {

        ServerSettings settings = new ServerSettings();

        settings.setHost("foo");
        settings.setPort(2222);

        assertEquals("foo", settings.host);
        assertEquals(2222, (int)settings.port);
    }
}
