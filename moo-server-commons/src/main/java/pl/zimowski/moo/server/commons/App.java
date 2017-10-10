package pl.zimowski.moo.server.commons;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pl.zimowski.moo.server.commons.jmx.JmxReportingSupport;

/**
 * Sever app. Bootstraps actual server engine, which depending on
 * implementation may operate on various protocols. A default moo server
 * protocol is web sockets.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@ComponentScan(basePackages = "pl.zimowski.moo")
@SpringBootApplication
public class App implements ApplicationRunner {

    @Inject
    private Logger log;

    @Inject
    private ChatService chatService;

    @Inject
    private JmxReportingSupport jmxReporer;


    public static final void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("initializing server...");

        jmxReporer.initialize();
        chatService.start();

        log.info("server terminated");
    }
}