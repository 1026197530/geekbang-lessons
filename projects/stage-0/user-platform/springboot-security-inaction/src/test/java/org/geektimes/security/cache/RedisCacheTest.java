package org.geektimes.security.cache;

import org.geektimes.security.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * {@link RedisCache} Test
 */
public class RedisCacheTest {

    private RedisCache redisCache;

    @Before
    public void setUp() {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
        Jedis jedis = jedisPool.getResource();
        redisCache = new RedisCache("user", jedis);
    }

    @Test
    public void test_putAndGetString() {
        String nameKey = "name";
        String nameValue = "fkz11111";
        redisCache.put(nameKey, nameValue);
        String actualNameValue = redisCache.get(nameKey, String.class);
        assertEquals(nameValue, actualNameValue);
    }

    @Test
    public void test_putAndGetLong() {
        String ageKey = "age";
        Long ageValue = 18L;
        redisCache.put(ageKey, ageValue);
        Long actualAgeValue = redisCache.get(ageKey, Long.class);
        assertEquals(ageValue, actualAgeValue);
    }

    @Test
    public void test_putAndGetUser() {
        User user = new User();
        user.setName("fkz");
        user.setId(1L);
        user.setEmail("5562316@qq.com");
        user.setPassword("123456");
        user.setPhoneNumber("15236862397");

        redisCache.put(user.getId(), user);
        User actualUserValue = redisCache.get(user.getId(), User.class);
        assertEquals(user, actualUserValue);
    }

    @Test
    public void test_get_withValueLoader() {
        String key = UUID.randomUUID().toString().replace("-", "");
        Callable<Long> valueLoader = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return 99L;
            }
        };

        Long actual = redisCache.get(key, valueLoader);
        assertEquals(Long.valueOf(99L), actual);
        assertEquals(Long.valueOf(99L), redisCache.get(key, Long.class));
    }

    @Test
    public void test_get_withValueLoader_hasValue() {
        String ageKey = "age";
        redisCache.put(ageKey, 18L);

        Callable<Long> valueLoader = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return 1L;
            }
        };
        Long storeValue = redisCache.get(ageKey, valueLoader);
        assertEquals(Long.valueOf(18L), storeValue);
    }

    @Test
    public void test_clear() {
        redisCache.put("name", "fkz");
        redisCache.put("age", 18L);
        redisCache.put("email", "5562316@qq.com");

        redisCache.clear();
        assertNull(redisCache.get("name", String.class));
        assertNull(redisCache.get("age", Long.class));
        assertNull(redisCache.get("email", String.class));
    }

    @Test
    public void test_getActualKeyBytes() {
        byte[] keyBytes = "name".getBytes(StandardCharsets.UTF_8);
        byte[] prefixBytes = "book:".getBytes(StandardCharsets.UTF_8);
        byte[] actualKeyBytes = redisCache.getMergeBytes(prefixBytes, keyBytes);
        String s = new String(actualKeyBytes, StandardCharsets.UTF_8);
        assertEquals("book:name", s);
    }

    @After
    public void tearDown() {
        redisCache.clear();
    }

}