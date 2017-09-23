 package pl.zimowski.moo.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Information that server can emit to a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerEvent implements Serializable {

	/**
	 * Name reported for messages authored by the server.
	 */
	public static final String AUTHOR = "server";
	
    private static final long serialVersionUID = -6175363790070655216L;

    private long timestamp;

    private String clientId;

    private ServerAction action;

    private String message;

    /**
     * Author which caused this event. Not every server event is caused by 
     * the server. In fact, most events are caused by clients and server 
     * simply echoes them back by broadcasting equivalent server event. 
     */
    private String author = ServerEvent.AUTHOR;
    
    private String note;

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

    public ServerEvent withAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Additional data associated with this an event. Often empty.
     * 
     * @param note free text that could mean different things depending on the context
     * @return metadata (note) associated with an event
     */
    public ServerEvent withNote(String note) {
    	this.note = note;
    	return this;
    }
    
    public ServerEvent withParticipantCount(int count) {
        participantCount = count;
        return this;
    }

    public ServerEvent withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDateTime() {
        return new Date(timestamp).toString();
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
     * @return id of a client which triggered this server event; may be
     *  {@code null} if server itself triggered the event
     */
    public String getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "ServerEvent [timestamp=" + timestamp + ", action=" + action + ", clientId=" + clientId + ", author=" + author
                + ", message=" + message + ", note=" + note + ", participantCount=" + participantCount + "]";
    }
}