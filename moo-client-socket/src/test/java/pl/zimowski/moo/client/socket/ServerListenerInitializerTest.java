package pl.zimowski.moo.client.socket;

import org.junit.Test;

/**
 * Ensures that {@link ServerListenerInitializer} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerListenerInitializerTest {

    /**
     * Naive test to ensure that submission indeed takes place. No need to
     * submit real listener, simple null will do as it will produce NPE if
     * submission took place.
     */
    @Test(expected = NullPointerException.class)
    public void shouldNotInitialize() {

        new ServerListenerInitializer().initialize(null);
    }
}
