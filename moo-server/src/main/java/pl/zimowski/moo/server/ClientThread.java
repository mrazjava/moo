package pl.zimowski.moo.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerEvent;

/**
 * Handler of a live connection between client and server. Listens for events
 * generated by a connected client and emits them onto a server. Likewise,
 * listens for events from the server and emits them back to a client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ClientThread extends Thread implements ClientNotification {

    private static final Logger log = LoggerFactory.getLogger(ClientThread.class);

    private String clientId;

    /**
     * connection tunnel over which communication takes place
     */
    private Socket socket;

    /**
     * server notification service
     */
    private EventBroadcasting serverNotifier;

    /**
     * snapshot of system clock indicating when this thread last processed
     * socket activity
     */
    private long lastActivity;


    /**
     * Constructs an live link between client and a server. The link is
     * established over a socket and server notifier. This is all that a
     * running thread needs to exchange information between client and a
     * server.
     *
     * @param socket connection established by the client
     * @param serverNotifier used to inform server about events received from the client
     */
    public ClientThread(Socket socket, EventBroadcasting serverNotifier) {
        this.socket = socket;
        this.serverNotifier = serverNotifier;
        clientId = UUID.randomUUID().toString();
        lastActivity = System.currentTimeMillis();
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void run() {

        try {
            while(true) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                lastActivity = System.currentTimeMillis();
                ClientEvent msg = (ClientEvent)ois.readObject();
                msg.withId(clientId);
                log.debug("in: {}", msg);
                serverNotifier.broadcast(this, msg);
                if(ClientAction.Disconnect == msg.getAction()) {
                    log.info("closing connection: {}", socket);
                    socket.close();
                    break;
                }
            }
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace(); // should not happen
        }
        catch(IOException e) {
            log.info("ejecting connection: {}", e.getMessage());
            serverNotifier.broadcast(this, new ClientEvent(ClientAction.Disconnect));
        }
    }

    @Override
    public long getLastActivity() {
        return lastActivity;
    }

    /**
     * @return {@code true} if active socket connection is up and operational,
     *  {@code false} if no further communication over socket is possible
     */
    public boolean isConnected() {
        return !socket.isClosed();
    }

    /**
     * Terminates connection with a client. After this call, no communication
     * is possible and essentially this thread is dead.
     */
    public void disconnect() {
        try {
            socket.close();
        }
        catch(IOException e) {
            log.warn("problem closing socket: {}", e.getMessage());
        }
    }

    @Override
    public boolean notify(ServerEvent event) {

        log.debug("processing {}", event);

        boolean status = true; // assume success

        try {
            ObjectOutputStream ois = new ObjectOutputStream(socket.getOutputStream());
            ois.writeObject(event);
            ois.flush();
        }
        catch (IOException e) {
            log.error("failed to read from server socket: {}", e.getMessage());
            status = false;
        }

        return status;
    }

    @Override
    public int hashCode() {

        int result = 23;
        result = 31 * result + (socket == null ? 0 : socket.hashCode());
        result = 31 * result + (serverNotifier == null ? 0 : serverNotifier.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ClientThread [socket=" + socket + "]";
    }
}