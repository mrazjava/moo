package pl.zimowski.moo.ui.shell.writer;

import java.util.Scanner;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.ui.shell.commons.ExecutionThrottling;
import pl.zimowski.moo.ui.shell.commons.ShutdownAgent;

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
    private EventHandler eventReporter;
    
    @Inject
    private ShutdownAgent shutdownAgent;
    
    @Inject
    private ExecutionThrottling throttler;
    

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

            if(!clientHandler.connect(eventReporter)) {
                return;
            }

            // allow connection thru while console buffers the output
            while(eventReporter.getClientId() == null) {
            	throttler.throttle();
            }

            EventHandler.LOG.info("({}) type nickname or just hit enter; ctrl-c to exit", EventHandler.AUTHOR);

            String nickName = scanner.nextLine();
            ClientEvent signinEvent = new ClientEvent(ClientAction.Signin);
            
            if(StringUtils.isNotBlank(nickName)) {
            	eventReporter.setNick(nickName);
            }
            else {
            	clientHandler.send(new ClientEvent(ClientAction.GenerateNick));
            }

            // wait until server responds with a nick; event reporter listens 
            // for nick confirmation and sets nick accordingly
            while(eventReporter.getNick() == null) {
                throttler.throttle();
            }
            
            signinEvent.setAuthor(eventReporter.getNick());
            clientHandler.send(signinEvent);

            ApiUtils.printPrompt();
            
            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                
                if(input.equals("moo:exit")) {
                	break; // in the future we should expand moo: into a managable predicates
                }
                
                ClientEvent event = new ClientEvent(ClientAction.Message, eventReporter.getNick(), input);
                log.debug("sending: {}", event);
                clientHandler.send(event);
                
                ApiUtils.printPrompt();
            }
        }
        
        shutdownAgent.initiateShutdown(0);
    }

    @PreDestroy
    public void shutdown() {

    	String nick = eventReporter.getNick();
    	
    	if(clientHandler.isConnected()) {
    	
	    	if(nick != null) { // may be null if connected, then exit before signin
	    		EventHandler.LOG.info("({}) done mooing? bye {}!", EventHandler.AUTHOR, nick);
	    		clientHandler.send(new ClientEvent(ClientAction.Signoff).withAuthor(nick));
	    	}
    	
            clientHandler.send(new ClientEvent(ClientAction.Disconnect));
        }
    }
}
