package pl.zimowski.moo.test.utils;

import static ch.qos.logback.classic.ClassicConstants.FINALIZE_SESSION_MARKER;

import org.junit.Assert;

/**
 * Dummy pojo used to invoke {@link MockLogger} at all levels.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class DummyLogWorker {

    static final String THE_QUICK = "the quick";

    static final String BROWN_FOX = "brown fox";

    static final String JUMPS_OVER = "jumps over";

    static final String THE_LAZY = "the lazy";

    static final String DOG = "dog";

    private TestLogging log;

    
    void logIt() {

        log.trace(THE_QUICK);
        log.debug(BROWN_FOX);
        log.info(JUMPS_OVER);
        log.warn(THE_LAZY);
        log.error(DOG);
    }
    
    /**
     * invoke all other (overriden) trace calls
     */
    void logAlternateTrace() {

        Assert.assertTrue(log.isTraceEnabled());
        Assert.assertTrue(log.isTraceEnabled(FINALIZE_SESSION_MARKER));
        
        log.trace(FINALIZE_SESSION_MARKER, null);
        log.trace("", 1);
        log.trace("", 1, 2, 3);
        log.trace("", new Exception());
        log.trace(FINALIZE_SESSION_MARKER, "", 1);
        log.trace(FINALIZE_SESSION_MARKER, "", 1, 2);
        log.trace(FINALIZE_SESSION_MARKER, "", new Exception());
        log.trace("", 1, 2);
        log.trace(FINALIZE_SESSION_MARKER, "", 1, 2, 3);
    }
    
    /**
     * invoke all other (overriden) debug calls
     */
    void logAlternateDebug() {
        
        Assert.assertTrue(log.isDebugEnabled());
        Assert.assertTrue(log.isDebugEnabled(FINALIZE_SESSION_MARKER));
        
        log.debug(FINALIZE_SESSION_MARKER, null);
        log.debug("", 1);
        log.debug("", 1, 2, 3);
        log.debug("", new Exception());
        log.debug(FINALIZE_SESSION_MARKER, "", 1);
        log.debug(FINALIZE_SESSION_MARKER, "", 1, 2);
        log.debug(FINALIZE_SESSION_MARKER, "", new Exception());
        log.debug("", 1, 2);
        log.debug(FINALIZE_SESSION_MARKER, "", 1, 2, 3);
    }
    
    /**
     * invoke all other (overriden) info calls
     */
    void logAlternateInfo() {

        Assert.assertTrue(log.isInfoEnabled());
        Assert.assertTrue(log.isInfoEnabled(FINALIZE_SESSION_MARKER));
        
        log.info(FINALIZE_SESSION_MARKER, null);
        log.info("", 1);
        log.info("", 1, 2, 3);
        log.info("", new Exception());
        log.info(FINALIZE_SESSION_MARKER, "", 1);
        log.info(FINALIZE_SESSION_MARKER, "", 1, 2);
        log.info(FINALIZE_SESSION_MARKER, "", new Exception());
        log.info("", 1, 2);
        log.info(FINALIZE_SESSION_MARKER, "", 1, 2, 3);
    }
    
    /**
     * invoke all other (overriden) warn calls
     */
    void logAlternateWarn() {

        Assert.assertTrue(log.isWarnEnabled());
        Assert.assertTrue(log.isWarnEnabled(FINALIZE_SESSION_MARKER));
        
        log.warn(FINALIZE_SESSION_MARKER, null);
        log.warn("", 1);
        log.warn("", 1, 2, 3);
        log.warn("", new Exception());
        log.warn(FINALIZE_SESSION_MARKER, "", 1);
        log.warn(FINALIZE_SESSION_MARKER, "", 1, 2);
        log.warn(FINALIZE_SESSION_MARKER, "", new Exception());
        log.warn("", 1, 2);
        log.warn(FINALIZE_SESSION_MARKER, "", 1, 2, 3);
    }
    
    /**
     * invoke all other (overriden) error calls
     */
    void logAlternateError() {

        Assert.assertTrue(log.isErrorEnabled());
        Assert.assertTrue(log.isErrorEnabled(FINALIZE_SESSION_MARKER));
        
        log.error(FINALIZE_SESSION_MARKER, null);
        log.error("", 1);
        log.error("", 1, 2, 3);
        log.error("", new Exception());
        log.error(FINALIZE_SESSION_MARKER, "", 1);
        log.error(FINALIZE_SESSION_MARKER, "", 1, 2);
        log.error(FINALIZE_SESSION_MARKER, "", new Exception());
        log.error("", 1, 2);
        log.error(FINALIZE_SESSION_MARKER, "", 1, 2, 3);
    }
}