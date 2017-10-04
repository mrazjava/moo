package pl.zimowski.moo.ui.shell.commons;

import javax.inject.Inject;

import org.slf4j.Logger;
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

    @Inject
    private Logger log;
    
    @Value("${shell.commons.throttle}") Long delay;
    
    
	@Override
    public void throttle() {
	    log.warn("sleeping {}ms ... zzzzzzzz", delay);
        delay(this.delay);
    }

    @Override
	public void throttle(long delay) {
        log.warn("sleeping {}ms ... zzzzzzzz", delay);
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
