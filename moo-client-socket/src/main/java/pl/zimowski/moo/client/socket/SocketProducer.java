package pl.zimowski.moo.client.socket;

import java.io.IOException;
import java.net.Socket;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Given internal configuration, generates a web socket.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class SocketProducer {

    @Inject
    private Logger log;

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private int port;

    private Socket socket;

    private String status;


    /**
     * Generates live web socket based on configured. Attempts to open
     * socket every time, therefore once invoked, it is up to the caller
     * to ensure that socket is closed before invoking again.
     *
     * @return socket on success; {@code null} on error
     */
    public Socket getSocket() {

        if(socket == null) {
            try {
                socket = new Socket(host, port);
                status = String.format("connected to %s@%d", host, port);
                log.debug(status);
            }
            catch(IOException e) {
                status = "SOCKET INIT FAILED: " + e.getMessage();
                log.error("could not connect to server: {}", status);
            }
        }

        return socket;
    }

    /**
     * @return host to which socket establishes connection
     */
    public String getSocketHost() {
        return host;
    }

    /**
     * @return port on which socket establishes connection
     */
    public int getSocketPort() {
        return port;
    }

    /**
     * Status of socket opening operation performed via {@link #getSocket()}. Note
     * this is not the current status of a socket as socket may have been closed
     * or otherwise corrupted. This status is simply indication if there was error
     * when socket was being opened or if opening a socket succeeded.
     *
     * @return status of the last invocation of {@code #getSocket()}
     */
    public String getStatus() {
        return status;
    }
}
