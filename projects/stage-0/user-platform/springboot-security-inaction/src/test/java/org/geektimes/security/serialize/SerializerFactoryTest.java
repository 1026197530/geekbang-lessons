package org.geektimes.security.serialize;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SerializerFactoryTest {

    @Test
    public void test_getSerializer() {
        Object longObj = 1L;
        Serializer longSerializer = SerializerFactory.getSerializer(longObj.getClass());
        assertNotNull(longSerializer);
        assertTrue(longSerializer instanceof LongSerializer);

        Object stringObj = "aaa";
        Serializer stringSerializer = SerializerFactory.getSerializer(stringObj.getClass());
        assertNotNull(stringSerializer);
        assertTrue(stringSerializer instanceof StringSerializer);
    }

}