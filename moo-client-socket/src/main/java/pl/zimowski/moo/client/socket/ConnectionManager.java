package pl.zimowski.moo.client.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientReporting;

/**
 * Socket based manager. Expects that server is capable talking over web
 * sockets.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ConnectionManager implements ConnectionManagement {

    @Inject
    private Logger log;

    @Inject
    private SocketProducer socketProducer;

    private Socket socket;

    private boolean connected;

    @Inject
    private ServerListenerInitializer serverListenerInit;


    @PostConstruct
    public void init() {
    	log.info("\n{}", ApiUtils.fetchResource("/logo"));
    }

    @PreDestroy
    public void cleanup() {
    	disconnect();
    }

    @Override
    public boolean connect(ClientReporting reporter) {

        String host = socketProducer.getSocketHost();
        int port = socketProducer.getSocketPort();

        log.info("establishing connection to {}:{}", host, port);

        socket = socketProducer.getSocket();

        if(socket != null) {
            reporter.onBeforeServerConnect(String.format("%s:%d", host, port));
            serverListenerInit.initialize(new ServerListener(socket, reporter));
            connected = true;
        }
        else {
            reporter.onConnectToServerError(socketProducer.getStatus());
        }

        return connected;
    }

    @Override
    public void disconnect() {
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void send(ClientEvent event) {

        if(socket == null) {
            log.warn("null socket; was connect() invoked? ignoring {}", event);
            return;
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(event);
            out.flush();
            log.debug("sent:\n{}", event);
        }
        catch(IOException e) {
            log.error("message transmission failed: {}", e.getMessage());
        }
    }
}