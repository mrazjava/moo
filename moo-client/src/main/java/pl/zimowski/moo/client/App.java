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

/**
 * Text based front end to a client of a Moo chat service.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@SpringBootApplication
public class App implements ApplicationRunner {

    @Inject
    private Logger log;

    private String nick;


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

            log.info("How do you want to moo? (type nickname or just hit enter)");
            ApiUtils.printPrompt();

            nick = scanner.nextLine();
            boolean customNick = false;

            if(StringUtils.isBlank(nick)) {
                nick = "anonymous";
                customNick = true;
            }

            if(log.isInfoEnabled()) {
                log.info((customNick ? "You're" : "Alright") + " \"{}\", go ahead and mooo (ctrl-c to exit)", nick);
            }
            ApiUtils.printPrompt();

            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                log.debug("sending: {}", input);
                ApiUtils.printPrompt();
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        if(nick != null) {
            log.info("(client) done mooing? bye {}!", nick);
        }
    }

    @Bean
    @Scope("prototype")
    static Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());

    }
}