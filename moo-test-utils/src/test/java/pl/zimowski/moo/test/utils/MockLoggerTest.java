package pl.zimowski.moo.test.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static pl.zimowski.moo.test.utils.DummyLogWorker.THE_QUICK;
import static pl.zimowski.moo.test.utils.DummyLogWorker.BROWN_FOX;
import static pl.zimowski.moo.test.utils.DummyLogWorker.JUMPS_OVER;
import static pl.zimowski.moo.test.utils.DummyLogWorker.THE_LAZY;
import static pl.zimowski.moo.test.utils.DummyLogWorker.DOG;

import java.util.Map;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.event.Level;

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
    private DummyLogWorker logWorker;

    @Spy
    private TestLogger testLogger;


    @After
    public void initResultsMap() {
        getResults().clear();
    }
    
    private Map<Level, String> getResults() {
        return testLogger.getResults();
    }

    @Test
    public void shouldLogEverything() {

        MockLogger.resetSilence();

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(5));
        assertThat(getResults(), hasEntry(Level.TRACE, THE_QUICK));
        assertThat(getResults(), hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
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

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(4));
        assertNull(getResults().get(Level.TRACE));
        assertThat(getResults(), hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogDebug() {

        MockLogger.silentDebugOn();

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(4));
        assertThat(getResults(), hasEntry(Level.TRACE, THE_QUICK));
        assertNull(getResults().get(Level.DEBUG));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogInfoUp() {

        MockLogger.silentLevel = Level.DEBUG;

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(3));
        assertNull(getResults().get(Level.TRACE));
        assertNull(getResults().get(Level.DEBUG));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogInfo() {

        MockLogger.silentInfoOn();

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(4));
        assertThat(getResults(), hasEntry(Level.TRACE, THE_QUICK));
        assertThat(getResults(), hasEntry(Level.DEBUG, BROWN_FOX));
        assertNull(getResults().get(Level.INFO));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogWarnUp() {

        MockLogger.silentLevel = Level.INFO;

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(2));
        assertNull(getResults().get(Level.TRACE));
        assertNull(getResults().get(Level.DEBUG));
        assertNull(getResults().get(Level.INFO));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogWarn() {

        MockLogger.silentWarnOn();

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(4));
        assertThat(getResults(), hasEntry(Level.TRACE, THE_QUICK));
        assertThat(getResults(), hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertNull(getResults().get(Level.WARN));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldLogErrorOnly() {

        MockLogger.silentLevel = Level.WARN;

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(1));
        assertNull(getResults().get(Level.TRACE));
        assertNull(getResults().get(Level.DEBUG));
        assertNull(getResults().get(Level.INFO));
        assertNull(getResults().get(Level.WARN));
        assertThat(getResults(), hasEntry(Level.ERROR, DOG));
    }

    @Test
    public void shouldNotLogError() {

        MockLogger.silentErrorOn();

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(4));
        assertThat(getResults(), hasEntry(Level.TRACE, THE_QUICK));
        assertThat(getResults(), hasEntry(Level.DEBUG, BROWN_FOX));
        assertThat(getResults(), hasEntry(Level.INFO, JUMPS_OVER));
        assertThat(getResults(), hasEntry(Level.WARN, THE_LAZY));
        assertNull(getResults().get(Level.ERROR));
    }

    @Test
    public void shouldLogNothing() {

        MockLogger.silentLevel = Level.ERROR;

        logWorker.logIt();

        assertThat(getResults().values(), hasSize(0));
    }
    
    @Test
    public void shouldLogAlternateTraceCalls() {
        
        TestLogger.resetSilence();
        logWorker.logAlternateTrace();
        assertEquals(9, testLogger.getTraceCount());
    }
    
    @Test
    public void shouldLogAlternateDebugCalls() {
        
        TestLogger.resetSilence();
        logWorker.logAlternateDebug();
        assertEquals(9, testLogger.getDebugCount());
    }
    
    @Test
    public void shouldLogAlternateInfoCalls() {

        TestLogger.resetSilence();
        logWorker.logAlternateInfo();
        assertEquals(9, testLogger.getInfoCount());        
    }
    
    @Test
    public void shouldLogAlternateWarnCalls() {
        
        TestLogger.resetSilence();
        logWorker.logAlternateWarn();
        assertEquals(9, testLogger.getWarnCount());
    }
    
    @Test
    public void shouldLogAlternateErrorCalls() {
        
        TestLogger.resetSilence();
        logWorker.logAlternateError();
        assertEquals(9, testLogger.getErrorCount());
    }
    
    @Test
    public void shouldGetLoggerName() {
        
        assertEquals(MockLogger.class.getName(), testLogger.getName());
    }
}