package org.geektimes.security.serialize;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DeserializerFactoryTest {

    @Test
    public void test_getDeserializer() {
        Object longObj = 1L;
        Deserializer longDeserializer = DeserializerFactory.getDeserializer(longObj.getClass());
        assertNotNull(longDeserializer);
        assertTrue(longDeserializer instanceof LongDeserializer);

        Object stringObj = "aaa";
        Deserializer stringDeserializer = DeserializerFactory.getDeserializer(stringObj.getClass());
        assertNotNull(stringDeserializer);
        assertTrue(stringDeserializer instanceof StringDeserializer);
    }

}