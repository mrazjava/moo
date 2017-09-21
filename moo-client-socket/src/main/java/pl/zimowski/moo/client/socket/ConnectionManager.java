package pl.zimowski.moo.client.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ApiUtils;
import pl.zimowski.moo.api.ClientEvent;
import pl.zimowski.moo.api.ClientListener;

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

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private int port;

    private Socket socket;
    
    boolean connected;


    @PostConstruct
    public void init() {
    	log.info("\n{}", ApiUtils.fetchResource("/logo"));
    }

    @PreDestroy
    public void cleanup() {
    	disconnect();
    }

    @Override
    public boolean connect(ClientListener clientListener) {

        log.info("establishing connection to {}:{}", host, port);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            socket = new Socket(host, port);
            executor.submit(new ServerListener(socket, clientListener));
            connected = true;
        }
        catch(IOException e) {
            log.error("could not connect to server: {}", e.getMessage());
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
        }
        catch(IOException e) {
            log.error("message transmission failed: {}", e.getMessage());
        }
    }
}