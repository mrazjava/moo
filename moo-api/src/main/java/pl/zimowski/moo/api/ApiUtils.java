package pl.zimowski.moo.api;

public class ApiUtils {

    /**
     * Given a resource path, reads the resource and returns its content as {@code String}. Capable
     * of fetching a local resource (such as when running exploaded), or packaged resource (such
     * as when resource is packaged inside a JAR).
     *
     * @param resourcePath of a file to read
     * @return content of a file or {@code null} if resource could not be fetched
     */
    public static String fetchResource(String resourcePath) {
        return null;
    }
}
