package pl.zimowski.moo.server.jms;

import java.util.Scanner;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.jms.JmsGateway;
import pl.zimowski.moo.server.commons.ChatService;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class JmsChatService implements ChatService {

    @Inject
    private Logger log;

    @Inject
    private JmsGateway jms;

    private boolean running;


    @Override
    public void start() {

        // model after jndi.properties example from https://dzone.com/articles/jms-activemq

        try(Scanner scanner = new Scanner(System.in)) {

            running = true;

            log.info("server running (type moo:exit to stop)");

            while(running) {
                String input = scanner.nextLine();
                if(input.equals("moo:exit")) {
                    jms.stop();
                    running = false;
                }
            }
        }
        catch(Exception e) {
            running = false;
            log.error("jms service init error", e);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop() {
        running = false;
    }
}