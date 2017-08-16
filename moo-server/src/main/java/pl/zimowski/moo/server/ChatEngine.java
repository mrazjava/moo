package pl.zimowski.moo.server;

import org.springframework.stereotype.Component;

/**
 * Core implementation of chat server based on web sockets.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ChatEngine implements ChatService {

    private boolean running;


    @Override
    public void start() {

        running = true;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}