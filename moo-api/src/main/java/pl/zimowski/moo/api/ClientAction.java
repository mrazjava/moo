package pl.zimowski.moo.api;

/**
 * Possible events client can emit to a server.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public enum ClientAction {

    /**
     * Client established connection to a server with identified end user.
     * After this operation user is ready to chat.
     */
    Signin,

    /**
     * Client emitted a chat message to a server on behalf of end user.
     */
    Message,

    /**
     * User terminated chat session.
     */
    Signoff,

    /**
     * Client terminated connection.
     */
    Disconnect
}
