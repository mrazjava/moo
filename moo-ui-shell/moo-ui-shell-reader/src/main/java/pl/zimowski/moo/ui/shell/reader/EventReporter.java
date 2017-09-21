package pl.zimowski.moo.ui.shell.reader;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class EventReporter implements ClientListener {
	
	private static final Logger log = LoggerFactory.getLogger(EventReporter.class);
	
	private String nick;
	
	@Inject
	private ClientHandling clientHandler;
	
	private String clientId;
	
	
	@Override
	public void onEvent(ServerEvent event) {

		log.debug(event.toString());
		
		String author = event.getAuthor();
		
        if(event.getAction() == ServerAction.ConnectionEstablished) {
        	clientId = event.getClientId();
        	App.LOG.info("({}): connected, client id: {}", author, clientId);
        }
        else if(event.getAction() == ServerAction.NickGenerated) {
            nick = event.getMessage();
            App.LOG.info("({}): You will be known as '{}'", author, nick);

        }
        else if(event.getAction() == ServerAction.ServerExit) {
        	App.LOG.info("({}) connection terminated by server; bye!", author);
        	clientHandler.disconnect();
        }
        else {
        	App.LOG.info("({}): {}", author, event.getMessage());
        }
	}
	
	String getClientId() {
		return clientId;
	}
	
	String getNick() {
		return nick;
	}
	
	void setNick(String nick) {
		this.nick = nick;
	}
}
