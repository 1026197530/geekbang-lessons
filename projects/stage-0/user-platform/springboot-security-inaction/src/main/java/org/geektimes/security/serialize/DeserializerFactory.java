package org.geektimes.security.serialize;

import org.geektimes.security.util.GenericUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class DeserializerFactory {

    private static final Map<Type, Deserializer> deserializerMap = new HashMap<>();

    static {
        ServiceLoader<Deserializer> loader = ServiceLoader.load(Deserializer.class);
        for (Deserializer deserializer : loader) {
            Type genericType = GenericUtil.getClassGenericType(deserializer.getClass(), 0);
            deserializerMap.put(genericType, deserializer);
        }
    }

    /**
     * 根据类型获取序列化器
     * @param clazz
     * @return
     */
    public static Deserializer getDeserializer(Class<?> clazz) {
        Deserializer deserializer = deserializerMap.get(clazz);
        if (deserializer == null) {
            // 如果获取不到，使用默认的序列化器
            deserializer = new GenericDeserializer();
        }
        return deserializer;
    }

}
