package pl.zimowski.moo.server.socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.stereotype.Component;

/**
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ServerSocketFactory {

    ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }
}
