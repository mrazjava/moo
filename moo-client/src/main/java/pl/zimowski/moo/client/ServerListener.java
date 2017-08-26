package pl.zimowski.moo.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.ApiUtils;
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

    private ConnectionManagement connectionManagement;

    private String clientId;


    public ServerListener(Socket socket, ConnectionManagement connectionManagement, String clientId) {
        this.socket = socket;
        this.connectionManagement = connectionManagement;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        log.debug("listening as client {}", clientId);

        try {
            while(!socket.isInputShutdown()) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ServerEvent serverEvent = (ServerEvent)in.readObject();
                App.LOG_CHAT.info("({}) {}", serverEvent.getAuthor(), serverEvent.getMessage());
                ApiUtils.printPrompt();
            }
        }
        catch(EOFException e) {
            App.LOG_CHAT.info("(client) connection terminated by server; bye!");
        }
        catch (IOException | ClassNotFoundException e) {
            log.error("unexpected connection error: {}; aborting!", e.getMessage());
        }

        connectionManagement.disconnect();
        System.exit(MAX_PRIORITY);
    }
}