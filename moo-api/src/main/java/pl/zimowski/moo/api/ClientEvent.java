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


    public ClientEvent(ClientAction action) {
    }

    public ClientEvent(ClientAction action, String author, String message) {
        this(action);
        withAuthor(author);
        withMessage(message);
    }

    public ClientEvent withAuthor(String author) {
        return this;
    }

    public ClientEvent withMessage(String message) {
        return this;
    }

    public ClientAction getAction() {
        return null;
    }

    public String getAuthor() {
        return null;
    }

    public String getMessage() {
        return null;
    }

    public long getTimestamp() {
        return 0;
    }
}