package pl.zimowski.moo.ui.shell.writer;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;

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
            	Thread.sleep(50);
            }

            log.info("How do you want to moo? (type nickname or just hit enter, ctrl-c to exit)");

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
            	Thread.sleep(50);
            }
            
            signinEvent.setAuthor(eventReporter.getNick());
            clientHandler.send(signinEvent);

            while(scanner.hasNextLine()) {

                String input = scanner.nextLine();
                ClientEvent event = new ClientEvent(ClientAction.Message, eventReporter.getNick(), input);
                log.debug("sending: {}", event);
                clientHandler.send(event);
            }
        }
    }

    @PreDestroy
    public void shutdown() {

    	String nick = eventReporter.getNick();
    	
    	if(clientHandler.isConnected()) {
    	
	    	if(nick != null) { // may be null if connected, then exit before signin
	    		EventHandler.LOG.info("(client) done mooing? bye {}!", nick);
	    		clientHandler.send(new ClientEvent(ClientAction.Signoff).withAuthor(nick));
	    	}
    	
            clientHandler.send(new ClientEvent(ClientAction.Disconnect));
        }
    }

    @Bean
    @Scope("prototype")
    static Logger logger(InjectionPoint injectionPoint){
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());

    }
}
