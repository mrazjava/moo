package pl.zimowski.moo.server.commons;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;

/**
 * @since 1.3.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class EventManager {

    @Inject
    private Logger log;
    
    @Inject
    private ServerUtils serverUtils;
    
    /**
     * Connected client is not the same as chat participant. For instance, a
     * web client may establish a single connection but may provide for many
     * participants.
     */
    private int participantCount;
    
    
    
    /**
     * Inspects the nature of a message received from a client, and updates
     * tracked totals as necessary. Transforms the client message to equivalent 
     * server message so that it can be used by the engine to rebroadcast it.
     *
     * @param clientEvent produced by the client
     * @return equivalent server message
     */
    public ServerEvent clientEventToServerEvent(ClientEvent clientEvent) {

        ClientAction clientAction = clientEvent.getAction();
        ServerAction serverAction = null;
        String serverMessage = null;
        String author = null;
        String note = null;
        
        log.debug("\n{}", clientEvent);

        switch(clientAction) {
            case Connect:
                serverAction = ServerAction.ConnectionEstablished;
                break;
            case Signin:
                serverAction = ServerAction.ParticipantCount;
                participantCount++;

                author = ServerEvent.AUTHOR;
                StringBuilder msg = new StringBuilder(String.format("%s joined;", clientEvent.getAuthor()));
                if(participantCount > 1)
                    msg.append(String.format(" %d participants", participantCount));
                else
                    msg.append(" and is the only participant so far");

                serverMessage = msg.toString();
                break;
            case Signoff:
                serverAction = ServerAction.ParticipantCount;
                participantCount--;
                author = ServerEvent.AUTHOR;
                serverMessage = String.format("%s left; %d participant(s) remaining", clientEvent.getAuthor(), participantCount);
                break;
            case Message:
                serverAction = ServerAction.Message;
                author = clientEvent.getAuthor();
                if(StringUtils.isEmpty(author)) author = ServerUtils.ANONYMOUS;
                serverMessage = String.format("%s", clientEvent.getMessage());
                break;
            case GenerateNick:
                serverAction = ServerAction.NickGenerated;
                author = ServerEvent.AUTHOR;
                serverMessage = serverUtils.randomNickName(0);
                break;
            case Disconnect:
                serverAction = ServerAction.ClientDisconnected;
                author = ServerEvent.AUTHOR;
                serverMessage = String.format("client disconnect: %s", clientEvent.getId());
                note = clientEvent.getId();
                break;
        }

        log.trace("participants: {}", participantCount);

        ServerEvent serverEvent = new ServerEvent(serverAction)
                .withParticipantCount(participantCount)
                .withClientId(clientEvent.getId());
        
        if(serverMessage != null) serverEvent.setMessage(serverMessage);
        if(author != null) serverEvent.withAuthor(author);
        if(note != null) serverEvent.setNote(note);
        
        log.debug("\n{}", serverEvent);
        
        return serverEvent;
    }
    
    public void incrementParticipantCount() {
        participantCount++;
    }
    
    public void decrementParticipantCount() {
        participantCount--;
    }
    
    public int getParticipantCount() {
        return participantCount;
    }
}
