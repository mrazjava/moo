package pl.zimowski.moo.ui.shell.writer;

import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.commons.ShutdownAgent;
import pl.zimowski.moo.ui.shell.commons.ExecutionThrottling;

/**
 * Text based UI client of a Moo chat service with write only 
 * capability. Allows to generate chat events. Requires a UI 
 * reader to display incoming chats.
 *
 * @since 1.2.0
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
    private EventHandler eventHandler;
    
    @Inject
    private ShutdownAgent shutdownAgent;
    
    @Inject
    private ExecutionThrottling throttler;
    
    /**
     * maximum number of throttling iterations; once reached, 
     * there is no more throttling ({@code null} means infinite 
     * throttling)
     */
    @Value("${shell.writer.throttle.max}")
    private Integer maxThrottleExecutions;


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

        try (Scanner scanner = new Scanner(System.in)) {

            if(!clientHandler.connect(eventHandler)) {
                return;
            }

            // allow connection thru while console buffers the output
            while(eventHandler.getClientId() == null) {
                if(throttler.isCountExceeded(maxThrottleExecutions)) {
                    log.warn("throttling limit exceeded! continuing...");
                    throttler.reset();
                    break;
                }
            	throttler.throttle();
            }

            EventHandler.LOG.info("({}) type nickname or just hit enter; ctrl-c to exit", EventHandler.AUTHOR);

            String nickName = scanner.nextLine();
            ClientEvent signinEvent = eventHandler.newSigninEvent();
            
            if(StringUtils.isNotBlank(nickName)) {
            	eventHandler.setNick(nickName);
            }
            else {
            	clientHandler.send(eventHandler.newGenerateNickEvent());
            }
            
            // wait until server responds with a nick; event reporter listens 
            // for nick confirmation and sets nick accordingly
            while(eventHandler.getNick() == null) {
                if(throttler.isCountExceeded(maxThrottleExecutions)) {
                    log.warn("throttling limit exceeded! continuing...");
                    throttler.reset();
                    break;
                }                
                throttler.throttle();
            }
            
            signinEvent.setAuthor(eventHandler.getNick());
            clientHandler.send(signinEvent);

            ApiUtils.printPrompt();
            
            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                
                if(input.equals("moo:exit")) {
                	break; // in the future we should expand moo: into a managable predicates
                }
                
                ClientEvent event = eventHandler.newMessageEvent(input);
                log.debug("sending: {}", event);
                clientHandler.send(event);
                
                ApiUtils.printPrompt();
            }
        }
        
        shutdownAgent.initiateShutdown(0);
    }

    @PreDestroy
    public void shutdown() {

    	String nick = eventHandler.getNick();
    	
    	if(clientHandler.isConnected()) {
    	
	    	if(nick != null) { // may be null if connected, then exit before signin
	    		EventHandler.LOG.info("({}) done mooing? bye {}!", EventHandler.AUTHOR, nick);
	    		clientHandler.send(eventHandler.newSignoffEvent());
	    	}
    	
            clientHandler.send(eventHandler.newDisconnectEvent());
        }
    }
}
