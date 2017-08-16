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

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ClientEvent [author=" + author + ", message=" + message + ", timestamp=" + timestamp + ", action="
                + action + "]";
    }
}