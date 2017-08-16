package pl.zimowski.moo.server;

import org.springframework.stereotype.Component;

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