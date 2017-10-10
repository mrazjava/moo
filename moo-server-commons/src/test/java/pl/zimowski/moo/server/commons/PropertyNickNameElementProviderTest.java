package pl.zimowski.moo.server.commons;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.zimowski.moo.server.commons.PropertyNickNameElementProvider;

/**
 * Ensures that {@link PropertyNickNameElementProvider} correctly 
 * provides nick name elements.
 * 
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>) 
 */
public class PropertyNickNameElementProviderTest {

    @Test
    public void shouldGetAdjectives() {
        
        PropertyNickNameElementProvider provider = new PropertyNickNameElementProvider();
        ReflectionTestUtils.setField(provider, "nickAdjectives", new String[] { "adj1", "adj2" });
        
        assertArrayEquals(new String[] { "adj1", "adj2" }, provider.getAdjectives());
    }
    
    @Test
    public void shouldGetNouns() {
        
        PropertyNickNameElementProvider provider = new PropertyNickNameElementProvider();
        ReflectionTestUtils.setField(provider, "nickNouns", new String[] { "noun1", "noun2" });
        
        assertArrayEquals(new String[] { "noun1", "noun2" }, provider.getNouns());
    }
}
