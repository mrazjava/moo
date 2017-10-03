package pl.zimowski.moo.client.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Ensures that {@link ServerListener} is properly setup for 
 * listening. This implementation kicks off the listener on a 
 * separate single thread.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerListenerInitializer {

    /**
     * Sets up server listener within the framework of execution so that 
     * it immediately starts listening for the events.
     * 
     * @param listener to be initialized
     */
    public void initialize(ServerListener listener) {
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(listener);
    }
}
