package pl.zimowski.moo.api;

/**
 * Possible events client can emit to a server.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public enum ClientAction {

    /**
     * Client established connection with the server. No user is signed in.
     * This action is optional and some server implementations establish
     * connection implicitly without this action. For example, a socket
     * based server will confirm established connection merely upon acceptance
     * of a socket (without this action). Other implementations such as JMS,
     * will confirm connection upon this action since JMS connection is
     * established upon connecting to a remote broker which both, client
     * and server have equal access to.
     */
    Connect,

    /**
     * Client established connection to a server with identified end user.
     * After this operation user is ready to chat.
     */
    Signin,

    /**
     * Client would like server to randomly generate user nick name.
     */
    GenerateNick,

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
