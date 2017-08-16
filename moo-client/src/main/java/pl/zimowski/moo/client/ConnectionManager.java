package pl.zimowski.moo.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.zimowski.moo.api.ClientEvent;

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


    @Override
    public boolean connect() {

        boolean status = false;
        log.info("establishing connection to {}:{}", host, port);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            socket = new Socket(host, port);
            Thread serverListener = new ServerListener(socket);
            executor.submit(serverListener);
            status = true;
        }
        catch(IOException e) {
            log.error("could not connect to server: {}", e.getMessage());
        }

        return status;
    }

    @PreDestroy
    public void cleanup() {
        // server will close the socket for us :-)
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