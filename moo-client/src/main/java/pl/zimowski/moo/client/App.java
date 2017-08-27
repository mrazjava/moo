package pl.zimowski.moo.client;

import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;

/**
 * Text based client UI of a Moo chat service.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@SpringBootApplication
public class App implements ApplicationRunner {

    public static final Logger LOG_CHAT = LoggerFactory.getLogger("CHAT_ECHO");

    @Inject
    private Logger log;

    private String nick;

    @Inject
    private ConnectionManagement connMgr;

    @Inject
    private ClientUtils clientUtils;


    /**
     * Application entry point.
     *
     * @param args such as config overrides as per Spring Boot features
     */
    public static final void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments arg0) throws Exception {

        log.info("\n{}", ApiUtils.fetchResource("/logo"));

        try (Scanner scanner = new Scanner(System.in)) {

            log.info("How do you want to moo? (type nickname or just hit enter, ctrl-c to exit)");

            nick = scanner.nextLine();

            if(StringUtils.isBlank(nick)) {
                nick = clientUtils.randomNickName();
            }

            if(!connMgr.connect()) {
                return;
            }

            connMgr.send(new ClientEvent(ClientAction.Signin).withAuthor(nick));

            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                ClientEvent event = new ClientEvent(ClientAction.Message, nick, input);
                log.debug("sending: {}", event);
                connMgr.send(event);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        if(connMgr.isConnected()) {
            LOG_CHAT.info("(client) done mooing? bye {}!", nick);
            connMgr.send(new ClientEvent(ClientAction.Signoff).withAuthor(nick));
        }
    }

    @Bean
    @Scope("prototype")
    static Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());

    }
}