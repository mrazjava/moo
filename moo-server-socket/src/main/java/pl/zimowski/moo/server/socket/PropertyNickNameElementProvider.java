package pl.zimowski.moo.server.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides nick names from Spring managed application property store.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class PropertyNickNameElementProvider implements NickNameElements {

    @Value("${nickAdjectives}")
    private String[] nickAdjectives;

    @Value("${nickNouns}")
    private String[] nickNouns;


    @Override
    public String[] getAdjectives() {
        return nickAdjectives;
    }

    @Override
    public String[] getNouns() {
        return nickNouns;
    }
}