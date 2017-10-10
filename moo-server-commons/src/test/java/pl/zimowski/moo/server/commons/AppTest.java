package pl.zimowski.moo.server.commons;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.zimowski.moo.server.commons.App;
import pl.zimowski.moo.server.commons.ChatService;
import pl.zimowski.moo.server.commons.jmx.JmxReportingSupport;
import pl.zimowski.moo.test.utils.MockLogger;
import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that server {@link App} correctly runs application 
 * entry point (sequence).
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class AppTest extends MooTest {

    @InjectMocks
    private App server;
    
    @Spy
    private MockLogger mockLog;
    
    @Mock
    private ChatService chatService;
    
    @Mock
    private JmxReportingSupport jmx;
    
    boolean jmxInitialized = false;
    
    boolean chatServiceStarted = false;
    
    
    @Test
    public void shouldRun() throws Exception {
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                jmxInitialized = true;
                return null;
            }
        }).when(jmx).initialize();
        
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                chatServiceStarted = true;
                return null;
            }
        }).when(chatService).start();
        
        server.run(null);
    }
}
