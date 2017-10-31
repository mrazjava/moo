package pl.zimowski.moo.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * Very handy mock which can be used to control injectable loggers inside
 * mockito tests. By default, it logs at all levels. All logging levels can be
 * easily controlled via static silence fields (eg: {@code MockLogger.silent*}).
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class MockLogger implements Logger {

    private static final Logger log = LoggerFactory.getLogger(MockLogger.class);

    /**
     * Disables {@link Level#TRACE} if set to {@code true}. Other levels may
     * or may not be silent depending on their own switch.
     */
    public static boolean silentTrace = false;

    /**
     * Disables {@link Level#DEBUG} if set to {@code true}. Other levels may
     * or may not be silent depending on their own switch.
     */
    public static boolean silentDebug = false;

    /**
     * Disables {@link Level#INFO} if set to {@code true}. Other levels may
     * or may not be silent depending on their own switch.
     */
    public static boolean silentInfo = false;

    /**
     * Disables {@link Level#WARN} if set to {@code true}. Other levels may
     * or may not be silent depending on their own switch.
     */
    public static boolean silentWarn = false;

    /**
     * Disables {@link Level#ERROR} if set to {@code true}. Other levels may
     * or may not be silent depending on their own switch.
     */
    public static boolean silentError = false;

    /**
     * Sets logging threshold. By default all levels are enabled.
     */
    public static Level silentLevel = null;


    /**
     * Disables {@link Level#TRACE}. All other levels are enabled.
     */
    public static void silentTraceOn() {

        silentTrace = true;
        silentDebug = silentInfo = silentWarn = silentError = false;
        silentLevel = null;
    }

    /**
     * Disables {@link Level#DEBUG}. All other levels are enabled.
     */
    public static void silentDebugOn() {

        silentDebug = true;
        silentTrace = silentInfo = silentWarn = silentError = false;
        silentLevel = null;
    }

    /**
     * Disables {@link Level#INFO}. All other levels are enabled.
     */
    public static void silentInfoOn() {

        silentInfo = true;
        silentTrace = silentDebug = silentWarn = silentError = false;
        silentLevel = null;
    }

    /**
     * Disables {@link Level#WARN}. All other levels are enabled.
     */
    public static void silentWarnOn() {

        silentWarn = true;
        silentTrace = silentDebug = silentInfo = silentError = false;
        silentLevel = null;
    }

    /**
     * Disables {@link Level#ERROR}. All other levels are enabled.
     */
    public static void silentErrorOn() {

        silentError = true;
        silentTrace = silentDebug = silentInfo = silentWarn = false;
        silentLevel = null;
    }

    /**
     * Disables silence for all levels (default configuration). After this
     * operation, regardless of previous configuration all log levels will be
     * enabled.
     */
    public static void resetSilence() {
        silentTrace = silentDebug = silentInfo = silentWarn = silentError = false;
        silentLevel = null;
    }

    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(marker);
    }

    @Override
    public void trace(String msg) {
        if(!isTraceSilent()) log.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        if(!isTraceSilent()) log.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if(!isTraceSilent()) log.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        if(!isTraceSilent()) log.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        if(!isTraceSilent()) log.trace(msg, t);
    }

    @Override
    public void trace(Marker marker, String msg) {
        if(!isTraceSilent()) log.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if(!isTraceSilent()) log.trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if(!isTraceSilent()) log.trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if(!isTraceSilent()) log.trace(marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if(!isTraceSilent()) log.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void debug(String msg) {
        if(!isDebugSilent()) log.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        if(!isDebugSilent()) {
            log.debug(format, arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if(!isDebugSilent()) log.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        if(!isDebugSilent()) log.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        if(!isDebugSilent()) log.debug(msg, t);
    }

    @Override
    public void debug(Marker marker, String msg) {
        if(!isDebugSilent()) log.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if(!isDebugSilent()) log.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if(!isDebugSilent()) log.debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if(!isDebugSilent()) log.debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if(!isDebugSilent()) log.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public void info(String msg) {
        if(!isInfoSilent()) log.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        if(!isInfoSilent()) log.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if(!isInfoSilent()) log.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        if(!isInfoSilent()) log.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        if(!isInfoSilent()) log.info(msg, t);
    }

    @Override
    public void info(Marker marker, String msg) {
        if(!isInfoSilent()) log.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if(!isInfoSilent()) log.info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if(!isInfoSilent()) log.info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if(!isInfoSilent()) log.info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if(!isInfoSilent()) log.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void warn(String msg) {
        if(!isWarnSilent()) log.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        if(!isWarnSilent()) log.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        if(!isWarnSilent()) log.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if(!isWarnSilent()) log.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        if(!isWarnSilent()) log.warn(msg, t);
    }

    @Override
    public void warn(Marker marker, String msg) {
        if(!isWarnSilent()) log.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if(!isWarnSilent()) log.warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if(!isWarnSilent()) log.warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if(!isWarnSilent()) log.warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if(!isWarnSilent()) log.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(marker);
    }

    @Override
    public void error(String msg) {
        if(!isErrorSilent()) log.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        if(!isErrorSilent()) log.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if(!isErrorSilent()) log.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        if(!isErrorSilent()) log.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        if(!isErrorSilent()) log.error(msg, t);
    }

    @Override
    public void error(Marker marker, String msg) {
        if(!isErrorSilent()) log.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if(!isErrorSilent()) log.error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if(!isErrorSilent()) log.error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if(!isErrorSilent()) log.error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if(!isErrorSilent()) log.error(marker, msg, t);
    }

    protected boolean isTraceSilent() {
        return silentTrace || silentLevel != null;
    }

    protected boolean isDebugSilent() {

        return silentDebug ||
                silentLevel == Level.DEBUG ||
                silentLevel == Level.INFO ||
                silentLevel == Level.WARN ||
                silentLevel == Level.ERROR;
    }

    protected boolean isInfoSilent() {

        return silentInfo ||
                silentLevel == Level.INFO ||
                silentLevel == Level.WARN ||
                silentLevel == Level.ERROR;
    }

    protected boolean isWarnSilent() {

        return silentWarn ||
                silentLevel == Level.WARN ||
                silentLevel == Level.ERROR;
    }

    protected boolean isErrorSilent() {
        return silentError || silentLevel == Level.ERROR;
    }
}