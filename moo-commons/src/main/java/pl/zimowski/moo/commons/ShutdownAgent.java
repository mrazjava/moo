package pl.zimowski.moo.commons;

import javax.inject.Inject;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Mechanism used to gracefully exit spring managed app upon
 * a request.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class ShutdownAgent {

	@Inject
	private ApplicationContext appContext;


	public int initiateShutdown(int statusCode) {
		return SpringApplication.exit(appContext, () -> statusCode);
	}
}
