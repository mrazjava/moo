package pl.zimowski.moo.server.commons;

/**
 * Operations needed to assemble random nickname.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface NickNameElements {

    /**
     * @return pool of available adjectives
     */
    String[] getAdjectives();

    /**
     * @return pool of available nouns
     */
    String[] getNouns();
}
