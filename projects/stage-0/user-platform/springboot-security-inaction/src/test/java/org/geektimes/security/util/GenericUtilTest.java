package org.geektimes.security.util;

import org.geektimes.security.serialize.LongSerializer;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertNotNull;

public class GenericUtilTest {

    @Test
    public void test_getClassGenericType() {
        Type classGenericType = GenericUtil.getClassGenericType(((Object) new LongSerializer()).getClass(), 0);
        assertNotNull(classGenericType);
    }

    @Test
    public void test_getInterfaceGenericType() {
        Callable<Long> valueLoader = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return 1L;
            }
        };
        Type interfaceGenericType = GenericUtil.getInterfaceGenericType(((Object) valueLoader).getClass(), 0);
        assertNotNull(interfaceGenericType);
    }

}