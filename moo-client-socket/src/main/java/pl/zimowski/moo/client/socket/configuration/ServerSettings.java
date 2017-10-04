package pl.zimowski.moo.client.socket.configuration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Spring managed configuration of server parameters. Getters intentionally
 * omitted to encourage direct value injection.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
@Validated
@ConfigurationProperties(prefix = "server")
public class ServerSettings {

    @NotNull(message = "server host is required")
    String host;

    @NotNull(message = "server port is required")
    Integer port;


    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}