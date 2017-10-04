package pl.zimowski.moo.client.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.ClientListener;
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

    private ClientListener clientListener;
    
    private boolean hardExit = true;


    public ServerListener(Socket socket, ClientListener clientListener) {
        this.socket = socket;
        this.clientListener = clientListener;
    }

    void setHardExit(boolean hardExit) {
        this.hardExit = hardExit;
    }

    @Override
    public void run() {

        try {
            while(!socket.isInputShutdown()) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ServerEvent serverEvent = (ServerEvent)in.readObject();
                clientListener.onEvent(serverEvent);
            }
        }
        catch(EOFException e) {
            log.info("server was shut down at administrator's request");
        }
        catch (IOException | ClassNotFoundException e) {
            log.error("unexpected connection error: {}; aborting!", e.getMessage());
        }

        if(hardExit) {
            System.exit(MAX_PRIORITY);
        }
    }
    
    public boolean isListening() {
    	return !socket.isClosed();
    }
}