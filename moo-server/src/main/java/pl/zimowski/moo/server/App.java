package pl.zimowski.moo.server;

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

import pl.zimowski.moo.server.jmx.JmxReportingSupport;

/**
 * Sever app. Bootstraps actual server engine, which depending on
 * implementation may operate on various protocols. A default moo server
 * protocol is web sockets.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@SpringBootApplication
public class App implements ApplicationRunner {

    public static final String SERVER_NAME = "moo";

    @Inject
    private Logger log;

    @Inject
    private ChatService server;

    @Inject
    private JmxReportingSupport jmxReporer;


    public static final void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("initializing server...");

        jmxReporer.initialize();
        server.start();
    }

    /**
     * Produces injectable logger.
     *
     * @param injectionPoint where log member is defined
     * @return log instance bound to injection point
     */
    @Bean
    @Scope("prototype")
    static Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());

    }
}