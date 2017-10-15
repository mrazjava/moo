package pl.zimowski.moo.api;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

/**
 * Information that client can emit to a server.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientEvent implements Serializable {

    private static final long serialVersionUID = -3135034051959415690L;

    private String author;

    private String message;

    private long timestamp;

    private ClientAction action;

    private String clientId;


    public ClientEvent(ClientAction action) {
        timestamp = System.currentTimeMillis();
        this.action = action;
    }

    public ClientEvent(ClientAction action, String author, String message) {
        this(action);
        withAuthor(author);
        withMessage(message);
    }

    public ClientEvent withAuthor(String author) {
        this.author = author;
        return this;
    }

    public ClientEvent withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ClientEvent withMessage(String message) {
        this.message = message;
        return this;
    }

    public ClientAction getAction() {
        return action;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Each client should provide best to its ability a unique id that can
     * allow it to recognize its own events broadcasted back by the server.
     * For example, a client can filter certain events from itself that server
     * echoed back. It is highly recommended this to be a UUID, in fact in 
     * the future String type of client id may be refactored to {@link UUID}.
     *
     * @return unique identifier that distinguishes this client from others
     */
    public String getClientId() {
        return clientId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null)
            return false;
        
        if(!(obj instanceof ClientEvent))
            return false;
        
        ClientEvent that = (ClientEvent)obj;
        
        return StringUtils.equals(clientId, that.clientId) &&
                StringUtils.equals(author, that.author) &&
                StringUtils.equals(message, that.message) &&
                timestamp == that.timestamp && 
                action == that.action;
    }

    @Override
    public String toString() {
        return "ClientEvent [clientId=" + clientId + ", author=" + author + ", message=" + message + ", timestamp=" + timestamp + ", action="
                + action + "]";
    }
}