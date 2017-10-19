package pl.zimowski.moo.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.springframework.beans.factory.InjectionPoint;

import pl.zimowski.moo.test.utils.MooTest;

/**
 * Ensures that {@link ComponentProducerTest} produces components as expected.
 *
 * @since 1.2.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
public class ComponentProducerTest extends MooTest {

    @InjectMocks
    private ComponentProducer producer;


    @Test
    public void shouldProduceLogger() throws NoSuchFieldException, SecurityException {

        Field field = getClass().getDeclaredField("producer");

        InjectionPoint ip = new InjectionPoint(field);
        Logger producedLogger = producer.logger(ip);

        assertNotNull(producedLogger);
        assertEquals(ComponentProducerTest.class.getName(), producedLogger.getName());
    }
}
