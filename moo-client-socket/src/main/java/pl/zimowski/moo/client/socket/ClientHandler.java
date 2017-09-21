package pl.zimowski.moo.client.socket;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientHandling;
import pl.zimowski.moo.api.ClientListener;

@Component
public class ClientHandler implements ClientHandling {

	private ClientListener clientListener;
	
	@Inject
	private ConnectionManagement connectionManager;
	
	@Override
	public void send(ClientEvent event) {
		connectionManager.send(event);
	}

	@Override
	public boolean connect(ClientListener listener) {
		return connectionManager.connect(listener);
	}

	@Override
	public boolean isConnected() {
		return connectionManager.isConnected();
	}

	@Override
	public void disconnect() {
		connectionManager.disconnect();
	}
	
	public ClientListener getClientListener() {
		return clientListener;
	}
}