package pl.zimowski.moo.ui.shell.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Ensures that {@link AbstractClientListener} operates as expected.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class AbstractClientListenerTest {

    @Test
    public void shouldRetainInformation() {
        
        AbstractClientListener listener = new AbstractClientListener() {
            
            @Override
            public void onEvent(ServerEvent event) {
            }
            
            @Override
            public String getAuthor() {
                return "moo";
            }
        };
        
        listener.setNick("baboon");
        listener.onBeforeServerConnect("localhost", 8001);
        listener.onConnectToServerError("ooops");
        listener.onEvent(new ServerEvent(ServerAction.ClientDisconnected));
        
        assertEquals("moo", listener.getAuthor());
        assertEquals("baboon", listener.getNick());
    }
}
