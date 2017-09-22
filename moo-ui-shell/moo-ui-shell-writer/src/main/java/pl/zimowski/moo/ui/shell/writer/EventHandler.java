package pl.zimowski.moo.ui.shell.writer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientListener;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Listens for critical server events which are required for 
 * proper operation of a writer.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class EventHandler implements ClientListener {

	static final Logger LOG = LoggerFactory.getLogger("CHAT_ECHO");
	
	private static final Logger log = LoggerFactory.getLogger(EventHandler.class);
	
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
        	LOG.info("({}): connected, client id: {}", author, clientId);
        }
        else if(event.getAction() == ServerAction.NickGenerated) {
            nick = event.getMessage();
            LOG.info("({}): You will be known as '{}'", author, nick);

        }
        else if(event.getAction() == ServerAction.ServerExit) {
        	LOG.info("({}) connection terminated by server; bye!", author);
        	clientHandler.disconnect();
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