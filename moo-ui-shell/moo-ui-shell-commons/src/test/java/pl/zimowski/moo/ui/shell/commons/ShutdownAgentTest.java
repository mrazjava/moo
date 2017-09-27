package pl.zimowski.moo.ui.shell.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;

import pl.zimowski.moo.ui.shell.commons.ShutdownAgent;

/**
 * Ensures that {@link ShutdownAgent} operates as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ShutdownAgentTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

	@InjectMocks
	private ShutdownAgent agent;
	
	@Mock
	private ApplicationContext context;
	
	
	@Test
	public void shouldShutDown() {
		
		int shutdownCode = 101;
		
		assertEquals(shutdownCode, agent.initiateShutdown(shutdownCode));
	}
}
