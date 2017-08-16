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
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerListener extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ServerListener.class);

    private Socket socket;

    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            while(!socket.isInputShutdown()) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ServerEvent serverEvent = (ServerEvent)in.readObject();
                log.info(serverEvent.getMessage());
                ApiUtils.printPrompt();
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