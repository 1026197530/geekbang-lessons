package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.*;
import java.util.Iterator;
import java.util.Set;


public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {


    private final StatefulRedisConnection connection;

    protected LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration,
                           StatefulRedisConnection connection) {
        super(cacheManager, cacheName, configuration);
        this.connection = connection;
    }



    protected V doGet(byte[] keyBytes) {
        RedisCommands<byte[], byte[]>           sync            = connection.sync();
        byte[] valueBytes = sync.get(keyBytes);
        V value = deserialize(valueBytes);
        return value;
    }



    @Override
    protected void doClose() {
        this.connection.close();
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        return false;
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        return null;
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> entry) throws CacheException, ClassCastException {

    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        return null;
    }

    @Override
    protected void clearEntries() throws CacheException {

    }

    @Override
    protected Set<K> keySet() {
        return null;
    }

    // 是否可以抽象出一套序列化和反序列化的 API
    private byte[] serialize(Object value) throws CacheException {
        byte[] bytes = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            // Key -> byte[]
            objectOutputStream.writeObject(value);
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new CacheException(e);
        }
        return bytes;
    }

    private V deserialize(byte[] bytes) throws CacheException {
        if (bytes == null) {
            return null;
        }
        V value = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            // byte[] -> Value
            value = (V)objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
        return value;
    }

}
