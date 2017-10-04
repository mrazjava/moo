package pl.zimowski.moo.api;

public interface ClientListener {

	void onEvent(ServerEvent event);
	
	void onBeforeServerConnect(String host, int port);
	
	void onConnectToServerError(String error);
}
