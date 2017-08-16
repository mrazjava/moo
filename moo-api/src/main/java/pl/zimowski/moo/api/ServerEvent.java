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

    private long timestamp;

    private ServerAction action;

    private String message;

    private int participantCount;


    public ServerEvent(ServerAction action) {
        this.action = action;
        timestamp = System.currentTimeMillis();
    }

    public ServerEvent(ServerAction action, String message) {
        this(action);
        withMessage(message);
    }

    public ServerEvent withMessage(String message) {
        this.message = message;
        return this;
    }

    public ServerEvent withParticipantCount(int count) {
        participantCount = count;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ServerAction getAction() {
        return action;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ServerEvent [timestamp=" + timestamp + ", action=" + action + ", message=" + message
                + ", participantCount=" + participantCount + "]";
    }
}
