/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.security.cache;

import org.geektimes.security.serialize.Deserializer;
import org.geektimes.security.serialize.DeserializerFactory;
import org.geektimes.security.serialize.Serializer;
import org.geektimes.security.serialize.SerializerFactory;
import org.geektimes.security.util.GenericUtil;
import org.springframework.cache.Cache;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Redis Cache 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 * Date : 2021-04-29
 */
public class RedisCache implements Cache {

    /**
     * name作为存储的前缀key, 实际的key为 name:{key}
     */
    private final String name;

    private final Jedis jedis;

    /**
     * 存储的前缀的字节数组
     */
    private final byte[] prefixBytes;

    /**
     * 使用list存储该{@link RedisCache}缓存的所有key
     */
    private final byte[] namespaceBytes;

    public RedisCache(String name, Jedis jedis) {
        Objects.requireNonNull(name, "The 'name' argument must not be null.");
        Objects.requireNonNull(jedis, "The 'jedis' argument must not be null.");
        this.name = name;
        this.jedis = jedis;
        prefixBytes = (this.name + ":").getBytes(StandardCharsets.UTF_8);
        namespaceBytes = ("namespace:" + this.name).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return jedis;
    }

    @Override
    public ValueWrapper get(Object key) {
        byte[] actualKeyBytes = getActualKeyBytes(key);
        byte[] valueBytes = jedis.get(actualKeyBytes);
        return valueBytes == null ? null : () -> valueBytes;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        byte[] actualKeyBytes = getActualKeyBytes(key);
        byte[] valueBytes = jedis.get(actualKeyBytes);
        return deserialize(valueBytes, type);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Type type = GenericUtil.getInterfaceGenericType(((Object) valueLoader).getClass(), 0);
        T t = (T) get(key, (Class) type);
        if (t == null) {
            try {
                t = valueLoader.call();
                put(key, t);
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
        }
        return t;
    }

    @Override
    public void put(Object key, Object value) {
        byte[] actualKeyBytes = getActualKeyBytes(key);
        byte[] valueBytes = serialize(value);
        jedis.set(actualKeyBytes, valueBytes);
        jedis.lpush(namespaceBytes, actualKeyBytes);
    }

    @Override
    public void evict(Object key) {
        byte[] actualKeyBytes = getActualKeyBytes(key);
        jedis.del(actualKeyBytes);
    }

    @Override
    public void clear() {
        List<byte[]> list = jedis.lrange(namespaceBytes, 0L, jedis.llen(namespaceBytes));
        if (list != null && !list.isEmpty()) {
            list.forEach(jedis::del);
        }
        jedis.del(namespaceBytes);
    }

    // 是否可以抽象出一套序列化和反序列化的 API
    private byte[] serialize(Object value) throws CacheException {
        Serializer serializer = SerializerFactory.getSerializer(value.getClass());
        return serializer.serialize(value);
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] bytes, Class<T> tClass) throws CacheException {
        Deserializer deserializer = DeserializerFactory.getDeserializer(tClass);
        return (T) deserializer.deserialize(bytes);
    }

    private byte[] getActualKeyBytes(Object key) {
        byte[] keyBytes = serialize(key);
        return getMergeBytes(prefixBytes, keyBytes);
    }

    /**
     * 返回一个合并的字节数组
     * @param prefixBytes 字节数组的前半部分
     * @param sourceBytes 字节数组的后半部分
     * @return
     */
    protected byte[] getMergeBytes(byte[] prefixBytes, byte[] sourceBytes) {
        byte[] result = new byte[prefixBytes.length + sourceBytes.length];
        System.arraycopy(prefixBytes, 0, result, 0, prefixBytes.length);
        System.arraycopy(sourceBytes, 0, result, prefixBytes.length, sourceBytes.length);
        return result;
    }

}
