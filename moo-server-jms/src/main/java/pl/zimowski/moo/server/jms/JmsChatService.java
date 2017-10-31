package pl.zimowski.moo.server.jms;

import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
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

    @Inject
    private ServerEventPublisher publisher;


    @Override
    public void start() {

        log.info("\n{}", ApiUtils.fetchResource("/logo"));

        try(Scanner scanner = new Scanner(System.in)) {

            running = true;

            log.info("server running (moo:exit or CTL-C to stop)");

            while(running) {
                String input = scanner.nextLine();
                if(input.equals("moo:exit")) {
                    notifyClients();
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

    @PreDestroy
    void notifyClients() {
        if(running) {
            publisher.publish(new ServerEvent(ServerAction.ServerExit));
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