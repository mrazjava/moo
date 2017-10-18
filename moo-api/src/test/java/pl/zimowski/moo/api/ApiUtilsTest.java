package pl.zimowski.moo.api;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Ensures that {@link ApiUtils} operates as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ApiUtilsTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    private Charset charset;


    @Test
    public void shouldFetchResource() {

        String expectedContent = "a dummy resource used to test ApiUtils";
        String fetchedContent = ApiUtils.fetchResource("/resource.txt");

        assertEquals(expectedContent, fetchedContent);
    }

    @Test
    public void shouldNotFetchResource() {

        ApiUtils.fetchResource("/blah-blah");
    }

    @Test
    public void shouldPrintPrompt() {

        SystemOutRule  systemOutMock = new SystemOutRule().mute().enableLog();
        systemOutMock.apply(new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ApiUtils.printPrompt();
                assertEquals("> ", systemOutMock.getLog());
            }
        }, null);
    }
}