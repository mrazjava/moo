package pl.zimowski.moo.ui.shell.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * In process delay of execution based on standard thread sleep.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
@Component
public class ThreadDelay implements ExecutionThrottling {

    private @Value("${shell.commons.throttle}") Long delay;
    
    
	@Override
    public void throttle() {
        delay(this.delay);
    }

    @Override
	public void throttle(long delay) {
	    delay(delay);
	}

	private void delay(long delay) {
	    
        try {
            Thread.sleep(delay);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
	}
}
