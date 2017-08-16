package pl.zimowski.moo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientAction;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ServerAction;
import pl.zimowski.moo.api.ServerEvent;
import pl.zimowski.moo.server.jmx.JmxReportingSupport;

/**
 * Core implementation of chat server based on web sockets. Supports multiple
 * clients and tracks their connections. Orchestrates incoming messages by
 * broadcasting each received client message to all other clients (essance of
 * chat service).
*
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ChatEngine implements ChatService, ServerNotification {

    @Inject
    private Logger log;

    @Value("${port}")
    private int port;

    private boolean running;

    @Inject
    private JmxReportingSupport jmxReporter;


    /**
     * each connected client will receive server events; this is the essence
     * of a chat server - ability to orchestrate events generated by clients
     * <p>
     * Note: this set is not thread safe so internal synchronization is needed
     * </p>
     */
    private Set<ClientNotification> connectedClients = new HashSet<>();


    @Override
    public void start() {

        log.info("\n{}", ApiUtils.fetchResource("/logo"));
        log.info("listening on port {} (ctrl-c to exit)", port);

        try (ServerSocket server = new ServerSocket(port)) {
            listen(server);
        }
        catch (IOException e) {
            log.error("starting server on port {} failed: {}", port, e.getMessage());
        }
    }

    /**
     * Given established server socket, enters listening mode over it. Listens
     * for incoming client socket connections and if detected, immediatelly
     * spawns a processing thread to handle that client.
     *
     * @param serverSocket on which to listen
     */
    private void listen(ServerSocket serverSocket) {

        ExecutorService executor = Executors.newCachedThreadPool();
        running = true;

        while(running) {
            try {
                Socket socket = serverSocket.accept();
                log.info("recieved {}", socket);
                ClientThread client = new ClientThread(socket, this);
                synchronized(this) {
                    connectedClients.add(client);
                }
                jmxReporter.clientConnected();
                log.debug("connected clients: {}", connectedClients.size());
                executor.submit(client);
            }
            catch(IOException e) {
                log.error("unexpected problem; aborting!", e);
            }
        }
    }

    /**
     * @return number of clients currently connected
     */
    public int getConnectedClientCount() {
        return connectedClients.size();
    }

    @Override
    public int notify(ClientThread clientThread, ClientEvent clientEvent) {

        log.debug("processing {} from {}", clientEvent, clientThread);

        ServerEvent serverEvent = processClientMessage(clientThread, clientEvent);
        int notifiedClients = 0;

        // could be perf bottleneck - should re-think for optimization; this
        // needs to be thread safe as otherwise iterator would get screwed up
        // and event delivery unpredictable (events eaten out)
        synchronized(this) {
            for(ClientNotification connectedClient : connectedClients) {
                if(connectedClient == clientThread && clientEvent.getAction() != ClientAction.Signin) {
                    continue;
                }
                log.debug("broadcasting to: {}", connectedClient);
                connectedClient.notify(serverEvent);
                notifiedClients++;
            }
        }

        log.debug("nofied {} clients", notifiedClients);

        return notifiedClients;
    }

    /**
     * Inspects the nature of a message received from a client, and updates
     * collection of connected clients as necessary. Transforms the client
     * message to equivalent server message so that it can be used by the
     * engine to rebroadcast it.
     *
     * @param clientThread which produced an event/message
     * @param clientEvent produced by the client
     * @return equivalent server message
     */
    private ServerEvent processClientMessage(ClientThread clientThread, ClientEvent clientEvent) {

        ClientAction clientAction = clientEvent.getAction();
        ServerAction serverAction = null;
        String serverMessage = null;

        switch(clientAction) {
            case Signin:
                serverAction = ServerAction.ParticipantCount;

                synchronized(this) {
                    connectedClients.add(clientThread);
                }

                if(connectedClients.size() > 1)
                    serverMessage = String.format("(%s) %s joined; %d participants", App.SERVER_NAME, clientEvent.getAuthor(), connectedClients.size());
                else
                    serverMessage = String.format("(%s) You're the only participant so far", App.SERVER_NAME);
                break;
            case Signoff:
                serverAction = ServerAction.ParticipantCount;

                synchronized(this) {
                    connectedClients.remove(clientThread);
                }
                jmxReporter.clientDisconnected();

                String exitInfo = String.format("(%s) %s left; ", App.SERVER_NAME, clientEvent.getAuthor());
                serverMessage = exitInfo +
                        (connectedClients.size() > 1 ?
                        String.format("%d participants", connectedClients.size()) :
                        "might be boring, you're all by yourself now :(");
                break;
            case Message:
                serverAction = ServerAction.Message;
                serverMessage = String.format("(%s): %s", clientEvent.getAuthor(), clientEvent.getMessage());
                break;
        }

        log.trace("connected clients: {}", connectedClients.size());

        return new ServerEvent(serverAction, serverMessage)
                .withParticipantCount(connectedClients.size());
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop() {
        running = false;
    }

    /**
     * Allows to override injected configured port value.
     *
     * @param port override to use instead of a configured one
     */
    public void setPort(int port) {
        this.port = port;
    }

    @PreDestroy
    public void shutdown() {
        log.info("shutting down; bye!");
    }
}