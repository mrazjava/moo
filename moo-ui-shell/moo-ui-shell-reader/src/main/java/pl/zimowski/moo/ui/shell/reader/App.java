package pl.zimowski.moo.ui.shell.reader;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.commons.ShutdownAgent;
import pl.zimowski.moo.ui.shell.commons.ExecutionThrottling;

/**
 * Text based UI client of a Moo chat service with read only 
 * capabilities.
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
    private ClientHandling clientHandler;
    
    @Inject
    private EventReporter eventReporter;
    
    @Inject
    private ExecutionThrottling throttler;

    /**
     * maximum number of throttling iterations; once reached, 
     * there is no more throttling ({@code null} means infinite 
     * throttling)
     */
    @Value("${shell.writer.throttle.max}")
    private Integer maxThrottleExecutions;
    
    @Inject
    private ShutdownAgent shutdownAgent;


    /**
     * Application entry point.
     *
     * @param args such as config overrides as per Spring Boot features
     */
    public static final void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(!clientHandler.connect(eventReporter)) {
            return;
        }

        // allow connection thru while console buffers the output
        while(eventReporter.getClientId() == null) {
            if(throttler.isCountExceeded(maxThrottleExecutions)) {
                log.error("could not obtain client id; aborting!");
                shutdownAgent.initiateShutdown(0);
                break;
            }
            throttler.throttle();
        }
    }

    @PreDestroy
    public void shutdown() {

    	if(clientHandler.isConnected()) {    	
            clientHandler.send(new ClientEvent(ClientAction.Disconnect));
        }
    }
}