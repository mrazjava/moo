package pl.zimowski.moo.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Ensures that {@link ClientUtils} performs as expected.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ServerUtilsTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @InjectMocks
    private ServerUtils clientUtils;

    @Mock
    private NickNameElements nickNameElements;


    @Test
    public void shouldGenerateRandomNickName() {

        given(nickNameElements.getAdjectives()).willReturn(new String[] { "Fantastic" });
        given(nickNameElements.getNouns()).willReturn(new String[] { "Donkey" });

        String randomNick = clientUtils.randomNickName();

        assertEquals("FantasticDonkey", randomNick);
    }
}
