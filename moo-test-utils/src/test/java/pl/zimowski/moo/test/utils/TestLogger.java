package pl.zimowski.moo.test.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * Wrapper over actual logger being tested with sole purpose of recording
 * results so that assertions can be made.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class TestLogger extends MockLogger implements TestLogging {

    private int traceCount;
    
    private int debugCount;
    
    private int infoCount;
    
    private int warnCount;
    
    private int errorCount;
    
    // logged messages are stored so that assertions can be made
    private Map<Level, String> results = new HashMap<>();
    

    Map<Level, String> getResults() {
        return results;
    }
    
    @Override
    public void trace(String msg) {
        super.trace(msg);
        if(!isTraceSilent()) {
            results.put(Level.TRACE, msg);
            traceCount++;
        }
    }

    @Override
    public void trace(String format, Object arg) {
        super.trace(format, arg);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        super.trace(format, arg1, arg2);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(String format, Object... arguments) {
        super.trace(format, arguments);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(String msg, Throwable t) {
        super.trace(msg, t);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(Marker marker, String msg) {
        super.trace(marker, msg);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        super.trace(marker, format, arg);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        super.trace(marker, format, arg1, arg2);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        super.trace(marker, format, argArray);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        super.trace(marker, msg, t);
        if(!isTraceSilent()) traceCount++;
    }

    @Override
    public int getTraceCount() {
        return traceCount;
    }

    @Override
    public void resetTraceCount() {
        traceCount = 0;
    }

    @Override
    public void debug(String msg) {
        super.debug(msg);
        if(!isDebugSilent()) {
            results.put(Level.DEBUG, msg);
            debugCount++;
        }
    }

    @Override
    public void debug(String format, Object arg) {
        super.debug(format, arg);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        super.debug(format, arg1, arg2);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(String format, Object... arguments) {
        super.debug(format, arguments);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(String msg, Throwable t) {
        super.debug(msg, t);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(Marker marker, String msg) {
        super.debug(marker, msg);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        super.debug(marker, format, arg);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        super.debug(marker, format, arg1, arg2);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        super.debug(marker, format, arguments);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        super.debug(marker, msg, t);
        if(!isDebugSilent()) debugCount++;
    }

    @Override
    public int getDebugCount() {
        return debugCount;
    }

    @Override
    public void resetDebugCount() {
        debugCount = 0;
    }

    @Override
    public void info(String msg) {
        super.info(msg);
        if(!isInfoSilent()) results.put(Level.INFO, msg);
    }

    @Override
    public void info(String format, Object arg) {
        super.info(format, arg);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        super.info(format, arg1, arg2);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(String format, Object... arguments) {
        super.info(format, arguments);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(String msg, Throwable t) {
        super.info(msg, t);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(Marker marker, String msg) {
        super.info(marker, msg);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        super.info(marker, format, arg);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        super.info(marker, format, arg1, arg2);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        super.info(marker, format, arguments);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        super.info(marker, msg, t);
        if(!isInfoSilent()) infoCount++;
    }

    @Override
    public int getInfoCount() {
        return infoCount;
    }

    @Override
    public void resetInfoCount() {
        infoCount = 0;
    }

    @Override
    public void warn(String msg) {
        super.warn(msg);
        if(!isWarnSilent()) results.put(Level.WARN, msg);
    }

    @Override
    public void warn(String format, Object arg) {
        super.warn(format, arg);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(String format, Object... arguments) {
        super.warn(format, arguments);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        super.warn(format, arg1, arg2);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(String msg, Throwable t) {
        super.warn(msg, t);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(Marker marker, String msg) {
        super.warn(marker, msg);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        super.warn(marker, format, arg);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        super.warn(marker, format, arg1, arg2);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        super.warn(marker, format, arguments);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        super.warn(marker, msg, t);
        if(!isWarnSilent()) warnCount++;
    }

    @Override
    public int getWarnCount() {
        return warnCount;
    }

    @Override
    public void resetWarnCount() {
        warnCount = 0;
    }

    @Override
    public void error(String msg) {
        super.error(msg);
        if(!isErrorSilent()) results.put(Level.ERROR, msg);
    }

    @Override
    public void error(String format, Object arg) {
        super.error(format, arg);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        super.error(format, arg1, arg2);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(String format, Object... arguments) {
        super.error(format, arguments);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(String msg, Throwable t) {
        super.error(msg, t);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(Marker marker, String msg) {
        super.error(marker, msg);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        super.error(marker, format, arg);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        super.error(marker, format, arg1, arg2);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        super.error(marker, format, arguments);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        super.error(marker, msg, t);
        if(!isErrorSilent()) errorCount++;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public void resetErrorCount() {
        errorCount = 0;
    }
}