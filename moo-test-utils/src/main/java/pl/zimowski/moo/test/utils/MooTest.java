package pl.zimowski.moo.test.utils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Recommended base for moo unit tests, especially if they are based on mockito. 
 * Setups basic mockito rule as well as other commonnalities such as required 
 * system props, etc. Super simple tests can probably do without this base, but 
 * in general, a moo test should extend this base.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public abstract class MooTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    
    @BeforeClass
    public static void setupUnit() {
        System.setProperty("LOG_DIR", "target/logs-test");
    }
}
