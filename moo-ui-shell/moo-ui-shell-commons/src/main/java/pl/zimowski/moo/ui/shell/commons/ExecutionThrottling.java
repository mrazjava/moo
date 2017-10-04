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
}
