package pl.zimowski.moo.server;

import java.util.Random;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Useful utility methods.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ServerUtils {

    @Inject
    private NickNameElements nickNameElementProvider;


    /**
     * Given configured adjective and noun pool, randomly choose one from each
     * to create a bazaar nick name (eg: "IncredibleDragon").
     *
     * @return randomly generated nick name
     */
    public String randomNickName() {

        String[] adjectives = nickNameElementProvider.getAdjectives();
        String[] nouns = nickNameElementProvider.getNouns();

        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        return StringUtils.join(adjective, noun);
    }
}
