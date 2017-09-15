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

    /**
     * Number of seconds of client inactivity allowed. If this limit is exceeded
     * server will terminate client connection. If not defined, server never
     * terminates client connection.
     */
    @SuppressWarnings("unused")
    private Integer evictionTimeout;

    @NotNull(message = "At least 1 adjective is required; eg: 'fantastic'")
    private String[] nickAdjectives;

    @NotNull(message = "At least 1 noun is required; eg: 'bull'")
    private String[] nickNouns;


    public void setPort(int port) {
        this.port = port;
    }

    public void setEvictionTimeout(Integer evictionTimeout) {
        this.evictionTimeout = evictionTimeout;
    }

    /**
     * Set of adjectives available to build anonymous nick name. Since nick
     * name is optional, but server requires it, we must have something.
     * Rather than having many "anonymous" users, it's more fun if we
     * construct a random anonymous name made up of one adjective and one
     * noun, for example "UltimateBaboon".
     *
     * @param nickAdjectives to use when building anonymous nick name
     */
    public void setNickAdjectives(String[] nickAdjectives) {
        this.nickAdjectives = nickAdjectives;
    }

    /**
     * Set of nouns available to build anonymous nick name. Since nick
     * name is optional, but server requires it, we must have something.
     * Rather than having many "anonymous" users, it's more fun if we
     * construct a random anonymous name made up of one adjective and one
     * noun, for example "ShyCow".
     *
     * @param nickAdjectives to use when building anonymous nick name
     */
    public void setNickNouns(String[] nickNouns) {
        this.nickNouns = nickNouns;
    }
}