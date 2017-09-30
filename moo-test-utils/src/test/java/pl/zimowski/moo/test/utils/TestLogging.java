package pl.zimowski.moo.test.utils;

import org.slf4j.Logger;

/**
 * Marker interface needed to make mockito and {@link TestLogger} happy.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public interface TestLogging extends Logger {
    
    /**
     * @return number of times any TRACE was invoked
     * @since 1.2.0
     */
    int getTraceCount();
    
    /**
     * Resets tracking of logged TRACE messages to starting point.
     * @since 1.2.0
     */
    void resetTraceCount();
    
    /**
     * @return number of times any DEBUG was invoked
     * @since 1.2.0
     */
    int getDebugCount();
    
    /**
     * Resets tracking of logged DEBUG messages to starting point.
     * @since 1.2.0
     */
    void resetDebugCount();
    
    /**
     * @return number of times any INFO was invoked
     * @since 1.2.0
     */
    int getInfoCount();
    
    /**
     * Resets tracking of logged TRACE messages to starting point.
     * @since 1.2.0
     */
    void resetInfoCount();
    
    /**
     * @return number of times any WARN was invoked
     * @since 1.2.0
     */
    int getWarnCount();
    
    /**
     * Resets tracking of logged TRACE messages to starting point.
     * @since 1.2.0
     */
    void resetWarnCount();
    
    /**
     * @return number of times any ERROR was invoked
     * @since 1.2.0
     */
    int getErrorCount();
    
    /**
     * Resets tracking of logged TRACE messages to starting point.
     * @since 1.2.0
     */
    void resetErrorCount();
}
