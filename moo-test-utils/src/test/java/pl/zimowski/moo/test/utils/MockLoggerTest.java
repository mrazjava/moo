package pl.zimowski.moo.test.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertNull;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import pl.zimowski.moo.test.utils.MockLogger;

/**
 * Ensures that {@link MockLogger} is correctly logging messages given various
 * configuration options.
 *
 * @since 2.5.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class MockLoggerTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @InjectMocks
    private DummyBean dummyBean;

    @Spy
    private TestLogger testLogger;

    private static final String THE_QUICK = "the quick";

    private static final String BROWN_FOX = "brown fox";

    private static final String JUMPS_OVER = "jumps over";

    private static final String THE_LAZY = "the lazy";

    private static final String DOG = "dog";

    // logged messages are stored so that assertions can be made
    private static Map<Level, String> results = new HashMap<>();


    @After
    public void initResultsMap() {
        results.clear();
    }

    @Test
    public void shouldLogEverything() {

        MockLogger.resetSilence();

        dummyBean.logIt();

        assertThat(results.values(), hasSize(5));
        assertThat(results, hasEntry(Level.TRACE, THE_QUICK));
        assertThat(results, hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogTrace() {

        MockLogger.silentTraceOn();
        testDebugUp();
    }

    @Test
    public void shouldLogDebugUp() {

        MockLogger.silentLevel = Level.TRACE;
        testDebugUp();
    }

    private void testDebugUp() {

        dummyBean.logIt();

        assertThat(results.values(), hasSize(4));
        assertNull(results.get(Level.TRACE));
        assertThat(results, hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogDebug() {

        MockLogger.silentDebugOn();

        dummyBean.logIt();

        assertThat(results.values(), hasSize(4));
        assertThat(results, hasEntry(Level.TRACE, THE_QUICK));
        assertNull(results.get(Level.DEBUG));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogInfoUp() {

        MockLogger.silentLevel = Level.DEBUG;

        dummyBean.logIt();

        assertThat(results.values(), hasSize(3));
        assertNull(results.get(Level.TRACE));
        assertNull(results.get(Level.DEBUG));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogInfo() {

        MockLogger.silentInfoOn();

        dummyBean.logIt();

        assertThat(results.values(), hasSize(4));
        assertThat(results, hasEntry(Level.TRACE, THE_QUICK));
        assertThat(results, hasEntry(Level.DEBUG, BROWN_FOX));
        assertNull(results.get(Level.INFO));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogWarnUp() {

        MockLogger.silentLevel = Level.INFO;

        dummyBean.logIt();

        assertThat(results.values(), hasSize(2));
        assertNull(results.get(Level.TRACE));
        assertNull(results.get(Level.DEBUG));
        assertNull(results.get(Level.INFO));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogWarn() {

        MockLogger.silentWarnOn();

        dummyBean.logIt();

        assertThat(results.values(), hasSize(4));
        assertThat(results, hasEntry(Level.TRACE, THE_QUICK));
        assertThat(results, hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertNull(results.get(Level.WARN));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogErrorOnly() {

        MockLogger.silentLevel = Level.WARN;

        dummyBean.logIt();

        assertThat(results.values(), hasSize(1));
        assertNull(results.get(Level.TRACE));
        assertNull(results.get(Level.DEBUG));
        assertNull(results.get(Level.INFO));
        assertNull(results.get(Level.WARN));
        assertThat(results, hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogError() {

        MockLogger.silentErrorOn();

        dummyBean.logIt();

        assertThat(results.values(), hasSize(4));
        assertThat(results, hasEntry(Level.TRACE, THE_QUICK));
        assertThat(results, hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(results, hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(results, hasEntry(Level.WARN, THE_LAZY));
        assertNull(results.get(Level.ERROR));
    }

    @Test
    public void shouldLogNothing() {

        MockLogger.silentLevel = Level.ERROR;

        dummyBean.logIt();

        assertThat(results.values(), hasSize(0));
    }

    /**
     * Dummy pojo used to invoke {@link MockLogger} at all levels.
     *
     * @since 2.5.0
     * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
     */
    static class DummyBean {

        private TestLogging log;

        public void logIt() {

            log.trace(THE_QUICK);
            log.debug(BROWN_FOX);
            log.info(JUMPS_OVER);
            log.warn(THE_LAZY);
            log.error(DOG);
        }
    }

    /**
     * Marker interface needed to make mockito and {@link TestLogger} happy.
     *
     * @since 2.5.0
     * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
     */
    static interface TestLogging extends Logger {
    }

    /**
     * Wrapper over actual logger being tested with sole purpose of recording
     * results so that assertions can be made.
     *
     * @since 2.5.0
     * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
     */
    static class TestLogger extends MockLogger implements TestLogging {

        @Override
        public void trace(String msg) {
            super.trace(msg);
            if(!isTraceSilent()) results.put(Level.TRACE, msg);
        }

        @Override
        public void debug(String msg) {
            super.debug(msg);
            if(!isDebugSilent()) results.put(Level.DEBUG, msg);
        }

        @Override
        public void info(String msg) {
            super.info(msg);
            if(!isInfoSilent()) results.put(Level.INFO, msg);
        }

        @Override
        public void warn(String msg) {
            super.warn(msg);
            if(!isWarnSilent()) results.put(Level.WARN, msg);
        }

        @Override
        public void error(String msg) {
            super.error(msg);
            if(!isErrorSilent()) results.put(Level.ERROR, msg);
        }
    }
}