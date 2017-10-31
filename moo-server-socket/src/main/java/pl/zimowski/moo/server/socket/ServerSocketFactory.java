package pl.zimowski.moo.server.socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.stereotype.Component;

/**
 * Produces {@link ServerSocket} on a given port. Useful as embedded component
 * which allows for easy mocking of socket server when necessary.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ServerSocketFactory {

    ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
}
