package pl.zimowski.moo.client.socket;

import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

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

    @Inject
    private NickNameManager nickNameManager;

    @Inject
    private ConnectionManagement connMgr;


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

            if(!connMgr.connect()) {
                return;
            }

            // allow connect to buffer console output before printing more
            Thread.sleep(300);

            log.info("How do you want to moo? (type nickname or just hit enter, ctrl-c to exit)");

            nickNameManager.setNickName(scanner.nextLine());

            connMgr.send(new ClientEvent(ClientAction.Signin)
                    .withAuthor(nickNameManager.getNickName())
                    );

            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                ClientEvent event = new ClientEvent(ClientAction.Message, nickNameManager.getNickName(), input);
                log.debug("sending: {}", event);
                connMgr.send(event);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        String nick = nickNameManager.getNickName();
        if(connMgr.isConnected() && nick != null) {
            LOG_CHAT.info("(client) done mooing? bye {}!", nick);
            if(nick != null) {
                connMgr.send(new ClientEvent(ClientAction.Signoff).withAuthor(nick));
            }
        }
        if(connMgr.isConnected()) {
            connMgr.send(new ClientEvent(ClientAction.Disconnect));
        }
    }

    @Bean
    @Scope("prototype")
    static Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());

    }
}