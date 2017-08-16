package pl.zimowski.moo.api;

import java.io.Serializable;

/**
 * Information that server can emit to a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEvent implements Serializable {

    private static final long serialVersionUID = -6175363790070655216L;


    public ServerEvent(ServerAction action) {
    }

    public ServerEvent(ServerAction action, String message) {
        this(action);
    }

    public ServerEvent withMessage(String message) {
        return this;
    }

    public ServerEvent withParticipantCount(int count) {
        return this;
    }

    public long getTimestamp() {
        return 0;
    }

    public ServerAction getAction() {
        return null;
    }

    public int getParticipantCount() {
        return 0;
    }

    public String getMessage() {
        return null;
    }
}
