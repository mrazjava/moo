package pl.zimowski.moo.server.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ClientUtils} performs as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerUtilsTest extends MooTest {

    @InjectMocks
    private ServerUtils clientUtils;

    @Mock
    private NickNameElements nickNameElements;


    @Test
    public void shouldGenerateCombinedRandomNickName() {

        given(nickNameElements.getAdjectives()).willReturn(new String[] { "Fantastic" });
        given(nickNameElements.getNouns()).willReturn(new String[] { "Donkey" });

        String randomNick = clientUtils.randomNickName(0);

        assertEquals("FantasticDonkey", randomNick);
    }
    
    @Test
    public void shouldGenerateNounNickName() {
        
        given(nickNameElements.getAdjectives()).willReturn(new String[] { "Crazy" });
        given(nickNameElements.getNouns()).willReturn(new String[] { "Baboon" });
        
        String randomNick = clientUtils.randomNickName(100);
        
        assertTrue(randomNick.endsWith("Baboon"));
    }
}
