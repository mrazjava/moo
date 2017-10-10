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
    
    private int count;
    
    /**
     * duration in ms to sleep for each throttle
     */
    @Value("${shell.commons.throttle}")
    Long delay;
    
    
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
    
    @Override
    public int getCount() {
        return count;
    }
    
    @Override
    public boolean isCountExceeded(Integer limit) {
        return limit != null && count > limit;
    }
    
    @Override
    public void reset() {
        count = 0;
    }

	private void delay(long delay) {
	    
        try {
            count++;
            Thread.sleep(delay);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
	}
}
