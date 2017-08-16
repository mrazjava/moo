package pl.zimowski.moo.server.configuration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Spring managed generic server config. Getters intentionally omitted to
 * encourage direct value injection.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
@Validated
@ConfigurationProperties("")
public class Settings {

    @NotNull(message = "server requires port to be running on!")
    private Integer port;


    public void setPort(int port) {
        this.port = port;
    }
}
