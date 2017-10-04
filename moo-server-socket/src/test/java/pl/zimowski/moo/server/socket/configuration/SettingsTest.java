package pl.zimowski.moo.server.socket.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Ensures that {@link Settings} reliably manages configuration.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class SettingsTest {

    @Test
    public void shouldRetainValues() {
        
        Settings settings = new Settings();
        
        assertNull(settings.getPort());
        assertNull(settings.getEvictionTimeout());
        assertNull(settings.getNickAdjectives());
        assertNull(settings.getNickNouns());
        
        settings.setPort(1234);
        settings.setEvictionTimeout(99999);
        settings.setNickAdjectives(new String[] { "Invisible", "Happy" });
        settings.setNickNouns(new String[] { "Baboon", "Fish" });
        
        assertEquals(Integer.valueOf(1234), settings.getPort());
        assertEquals(Integer.valueOf(99999), settings.getEvictionTimeout());
        assertTrue(2 == settings.getNickAdjectives().length);
        assertTrue(2 == settings.getNickNouns().length);
    }
}
