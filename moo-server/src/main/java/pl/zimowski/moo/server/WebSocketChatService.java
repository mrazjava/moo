package pl.zimowski.moo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
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
public class WebSocketChatService implements ChatService, EventBroadcasting {

    @Inject
    private Logger log;

    @Value("${port}")
    private int port;

    @Value("${evictionTimeout}")
    private Integer evictionTimeout;

    private boolean running;

    @Inject
    private JmxReportingSupport jmxReporter;

    @Inject
    private ServerUtils serverUtils;


    /**
     * each connected client will receive server events; this is the essence
     * of a chat server - ability to orchestrate events generated by clients
     * <p>
     * Note: this set is not thread safe so internal synchronization is needed
     * </p>
     */
    private Set<ClientNotification> connectedClients = new HashSet<>();

    /**
     * Connected client is not the same as chat participant. For instance, a
     * web client may establish a single connection but may provide for many
     * participants.
     */
    private int participantCount;


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
                log.info("received {}", socket);
                ClientThread client = new ClientThread(socket, this);
                synchronized(this) {
                    connectedClients.add(client);
                }
                jmxReporter.clientConnected();
                log.debug("connections: {}, participants: {}", connectedClients.size(), participantCount);
                executor.submit(client);
                client.notify(new ServerEvent(ServerAction.ConnectionEstablished)
                        .withClientId(client.getClientId())
                        );
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
    public int broadcast(ClientThread clientThread, ClientEvent clientEvent) {

        evictInactiveClients();

        log.debug("broadcasting {} from {}", clientEvent, clientThread);

        verifySignin(clientThread, clientEvent);

        ServerEvent serverEvent = clientEventToServerEvent(clientThread, clientEvent);
        int notifiedClients = 0;

        if(clientEvent.getAction() == ClientAction.Disconnect) {
            connectedClients.remove(clientEvent);
            return notifiedClients;
        }

        // could be perf bottleneck - should re-think for optimization; this
        // needs to be thread safe as otherwise iterator would get screwed up
        // and event delivery unpredictable (events eaten out)
        synchronized(this) {
            // standard message broadcast
            for(ClientNotification connectedClient : connectedClients) {

                log.debug("broadcasting to: {}", connectedClient);

                if(connectedClient.notify(serverEvent)) {
                    notifiedClients++;
                }
            }
        }

        log.debug("nofied {} clients", notifiedClients);

        return notifiedClients;
    }

    /**
     * Ensures that user is always signed in with a valid nick name. Client
     * may opt to produce signin event without a user nick name in which case
     * server must ensure that one is provided (randomly).
     *
     * @param clientThread associated with the client attached to the user
     * @param clientEvent to verify (and modify if necessary)
     */
    private void verifySignin(ClientThread clientThread, ClientEvent clientEvent) {

        if(clientEvent.getAction() == ClientAction.Signin && StringUtils.isBlank(clientEvent.getAuthor())) {
            String nick = serverUtils.randomNickName();
            clientThread.notify(new ServerEvent(ServerAction.NickGenerated).withMessage(nick));
            clientEvent.setAuthor(nick);
        }
    }

    private synchronized void evictInactiveClients() {

        for(Iterator<ClientNotification> iterator = connectedClients.iterator(); iterator.hasNext();) {

            ClientNotification connectedClient = iterator.next();
            DateTime lastActive = new DateTime(connectedClient.getLastActivity());
            int inactiveSeconds = Seconds.secondsBetween(lastActive, new DateTime()).getSeconds();

            if(evictionTimeout != null && inactiveSeconds > evictionTimeout) {
                log.info("{} inactive for {} seconds, evicting!", connectedClient, inactiveSeconds);
                connectedClient.notify(new ServerEvent(ServerAction.ConnectionTimeOut).withMessage("disconnected due to inactivity"));
                connectedClient.disconnect();
                iterator.remove();
                participantCount--;
                continue;
            }
        }
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
    private ServerEvent clientEventToServerEvent(ClientThread clientThread, ClientEvent clientEvent) {

        ClientAction clientAction = clientEvent.getAction();
        ServerAction serverAction = null;
        String serverMessage = null;
        String author = null;

        switch(clientAction) {
            case Signin:
                serverAction = ServerAction.ParticipantCount;
                participantCount++;

                author = ApiUtils.APP_NAME;
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
                author = ApiUtils.APP_NAME;
                serverMessage = String.format("%s left; %d participant(s) remaining", clientEvent.getAuthor(), participantCount);
                break;
            case Message:
                serverAction = ServerAction.Message;
                author = clientEvent.getAuthor();
                serverMessage = String.format("%s", clientEvent.getMessage());
                break;
            case Disconnect:
                synchronized(this) {
                    connectedClients.remove(clientThread);
                }
                break;
        }

        log.trace("clients: {}, participants: {}", connectedClients.size(), participantCount);

        return new ServerEvent(serverAction, serverMessage)
                .withParticipantCount(participantCount)
                .withAuthor(author)
                .withClientId(clientEvent.getId());
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
        log.debug("engine shutdown ({} clients, {} participants)", getConnectedClientCount(), participantCount);
    }
}