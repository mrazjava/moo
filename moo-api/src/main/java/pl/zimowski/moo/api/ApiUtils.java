package pl.zimowski.moo.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handy utility calls common to both server and client.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ApiUtils {

    private static final Logger log = LoggerFactory.getLogger(ApiUtils.class);

    public static final String APP_NAME = "moo";

    /**
     * Given a resource path, reads the resource and returns its content as {@code String}. Capable
     * of fetching a local resource (such as when running exploaded), or packaged resource (such
     * as when resource is packaged inside a JAR).
     *
     * @param resourcePath of a file to read
     * @return content of a file or {@code null} if resource could not be fetched
     */
    public static String fetchResource(String resourcePath) {

        InputStream is = ApiUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if(is == null) is = ApiUtils.class.getResourceAsStream(resourcePath);
        if(is == null) log.warn("invalid resource: {}", resourcePath);

        String fetchedContent = null;

        if(is != null) {
            log.debug("loading resource: {}", resourcePath);
            try {
                fetchedContent = IOUtils.toString(is, Charset.defaultCharset());
                log.trace("fetched content:\n{}", fetchedContent);
            }
            catch(IOException e) {
                log.error("problem reading resource [{}]: {}", resourcePath, e.getMessage());
            }
        }

        return fetchedContent;
    }

    /**
     * Simulates command line prompt by printing a &gt; symbol to the
     * console using {@link System#out} followed by one space (and no line
     * break). This is just additional eye candy that can make a boring console
     * app more appealing.
     */
    public static void printPrompt() {
        System.out.print("> ");
    }
}