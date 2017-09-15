package pl.zimowski.moo.api;

/**
 * Possible events server can emit to a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public enum ServerAction {

    ConnectionEstablished,

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
     * chat message from another user
     */
    Message
}
