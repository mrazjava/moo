package pl.zimowski.moo.ui.shell.commons;

/**
 * Delay of application execution.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public interface ExecutionThrottling {

    /**
     * Delay execution by some pre-configured time period.
     */
    void throttle();
    
	/**
	 * Delay execution by desired period of time.
	 * 
	 * @param delay in milliseconds
	 */
	void throttle(long delay);
	
	/**
	 * @return number of times throttle was invoked
	 */
	int getCount();
	
	/**
	 * Reset throttle counter to starting point as if no throttling 
	 * was performed.
	 */
	void reset();
	
	/**
	 * Determines if number of throttle invocations exceeded some limit.
	 * 
	 * @param limit of allowed throttling executions
	 * @return {@code true} if throttle was executed beyond allowed limit
	 */
	boolean isCountExceeded(Integer limit);
}
