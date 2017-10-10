package pl.zimowski.moo.server.commons;

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

    public static final String ANONYMOUS = "AnonymousCoward";

    @Inject
    private NickNameElements nickNameElementProvider;


    /**
     * Given configured adjective and noun pool, randomly choose one from each
     * to create a bazaar nick name (eg: "IncredibleDragon").
     *
     * @param adjectiveSkip probability with which adjective will be skipped 
     *  relative to the size of adjective list (0 means adjective will always be 
     *  generated, if adjective list is of size 1, then 1 means there is 50% 
     *  probability that it will be skipped); negative value is ignored
     * @return randomly generated nick name
     */
    public String randomNickName(int adjectiveSkip) {

        if(adjectiveSkip < 0) adjectiveSkip = 0;
        
        String[] adjectives = nickNameElementProvider.getAdjectives();
        String[] nouns = nickNameElementProvider.getNouns();

        Random random = new Random();
        int adjectiveIndex = random.nextInt(adjectives.length + adjectiveSkip);
        String adjective = null;
        
        if(adjectiveIndex < adjectives.length) {
            adjective = adjectives[adjectiveIndex];
        }
        String noun = nouns[random.nextInt(nouns.length)];
        return StringUtils.join(adjective, noun);
    }
}
