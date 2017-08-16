package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Ensures that {@link ApiUtils} operates as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ApiUtilsTest {

    @Test
    public void shouldFetchResource() {

        String expectedContent = "a dummy resource used to test ApiUtils";
        String fetchedContent = ApiUtils.fetchResource("/resource.txt");

        assertEquals(expectedContent, fetchedContent);
    }
}
