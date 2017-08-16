package pl.zimowski.moo.client.configuration;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Spring managed configuration of client parameters. Getters intentionally
 * omitted to encourage direct value injection.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
@Validated
@ConfigurationProperties(prefix = "client")
public class ClientSettings {

    @NotNull(message = "At least 1 adjective is required; eg: 'fantastic'")
    private String[] nickAdjectives;

    @NotNull(message = "At least 1 noun is required; eg: 'bull'")
    private String[] nickNouns;


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
