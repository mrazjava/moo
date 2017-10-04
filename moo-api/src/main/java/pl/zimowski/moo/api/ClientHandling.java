package pl.zimowski.moo.api;

/**
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ClientHandling {

	boolean connect(ClientListener listener);
	
	boolean isConnected();
	
	void disconnect();
	
	void send(ClientEvent event);
}
