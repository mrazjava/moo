package pl.zimowski.moo.ui.shell.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.ClientListener;

/**
 * Recommended base for shell based listeners for server events.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public abstract class AbstractClientListener implements ClientListener {

	public static final Logger LOG = LoggerFactory.getLogger("CHAT_ECHO");
	
	protected String nick;
	
	protected String clientId;
	
	/**
	 * Name to be reported when messages are generated by UI listener. 
	 * Not to confuse with chat user (author of chat events).
	 * 
	 * @return author reported as listener (eg: reader or writer)
	 */
	public abstract String getAuthor();
	
	@Override
	public void onBeforeServerConnect(String host, int port) {
		LOG.info("({}) establishing connection to {}:{}", getAuthor(), host, port);
	}

	@Override
	public void onConnectToServerError(String error) {
		LOG.info("(()) could not establish server connection: {}", getAuthor(), error);
	}

	public String getClientId() {
		return clientId;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
}