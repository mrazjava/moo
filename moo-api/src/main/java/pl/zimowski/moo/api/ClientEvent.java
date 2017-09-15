package pl.zimowski.moo.api;

import java.io.Serializable;

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

    private String id;


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

    public ClientEvent withId(String id) {
        this.id = id;
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
     * echoed back. It is highly recommended this to be a UUID.
     *
     * @return unique identifier that distinguishes this client from others
     */
    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ClientEvent [id=" + id + ", author=" + author + ", message=" + message + ", timestamp=" + timestamp + ", action="
                + action + "]";
    }
}