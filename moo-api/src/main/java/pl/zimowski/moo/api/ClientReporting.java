package pl.zimowski.moo.api;

/**
 * Ability to report on chat events, including server generated events. The
 * actual reporting is performed by the client, which being connected to the
 * server, is able to rely server events as well. (U)ser (I)nterfaces will
 * implement this to report back on chat activity. Server will generate
 * events in response to activity on a client side, or when interesting
 * statistics are to be reported, or when server itself has information to
 * report about its state.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface ClientReporting {

	/**
	 * Fired when client detected a server event. Most server events are
	 * generated as a result of a client event. Some events a client may want to
	 * filter (ignore) but it is entirely up to the client implementation.
	 *
	 * @param event generated by the server
	 */
	void onEvent(ServerEvent event);

	/**
	 * Fired just before a client attempts connection with the server. Depending on
	 * the implementation, server may be actually hosted on specific host and port (eg:
	 * websockets), but in some cases this may not be true. For instance, JMS based
	 * servers, do not host a connection themselves as they share the same JMS broker
	 * with a client (in case of JMS this call could be invoked before connecting to a
	 * JMS broker).
	 *
	 * @param uri of the server
	 */
	void onBeforeServerConnect(String uri);

	/**
	 * Fired if client could not connect with a server.
	 *
	 * @param error message associated with the failure.
	 */
	void onConnectToServerError(String error);
}
