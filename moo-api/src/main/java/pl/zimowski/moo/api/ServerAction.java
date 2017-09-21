package pl.zimowski.moo.api;

/**
 * Possible events server can emit to a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public enum ServerAction {

    /**
     * Upon client request to establish connection with the server, 
     * server confirmed successful connection.
     */
    ConnectionEstablished,

    /**
     * Upon client request to generate a random nick name, server generated 
     * the nick name.
     */
    NickGenerated,

    /**
     * report count of online users due to change to client collection (login,
     * logout, timeout)
     */
    ParticipantCount,

    /**
     * server aborted client connection due to inactivity
     */
    ConnectionTimeOut,
    
    /**
     * Client voluntarily chose to disconnect.
     */
    ClientDisconnected,

    /**
     * chat message from another user
     */
    Message,
    
    /**
     * Server process terminated and all client connections were aborted
     */
    ServerExit
}
