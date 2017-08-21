package pl.zimowski.moo.ui.web1;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import pl.zimowski.moo.api.ServerEvent;

/**
 * Listens for incoming events from the server and echoes them back to the
 * client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerListener extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ServerListener.class);

    private Socket socket;

    private SimpMessageSendingOperations sender;

    private LinkedList<ServerEvent> recentMessages;


    public ServerListener(Socket socket, SimpMessageSendingOperations sender) {
        this.socket = socket;
        this.sender = sender;
        recentMessages = new LinkedList<>();
    }

    public List<ServerEvent> getRecentEvents() {
        return Collections.unmodifiableList(recentMessages);
    }

    @Override
    public void run() {

        try {
            while(true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ServerEvent serverEvent = (ServerEvent)in.readObject();

                log.info(serverEvent.toString());

                if(recentMessages.size() == 15)
                    recentMessages.removeFirst();

                recentMessages.add(serverEvent);

                sender.convertAndSend("/topic/viewchats", recentMessages);
            }
        }
        catch(EOFException e) {
            log.info("(client) connection terminated by server; bye!");
        }
        catch (IOException | ClassNotFoundException e) {
            log.error("unexpected connection error: {}; aborting!", e.getMessage());
        }
    }
}