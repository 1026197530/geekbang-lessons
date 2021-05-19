package org.geektimes.security.serialize;

import org.geektimes.security.util.GenericUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class SerializerFactory {

    private static final Map<Type, Serializer> serializerMap = new HashMap<>();

    static {
        ServiceLoader<Serializer> loader = ServiceLoader.load(Serializer.class);
        for (Serializer serializer : loader) {
            Type genericType = GenericUtil.getClassGenericType(serializer.getClass(), 0);
            serializerMap.put(genericType, serializer);
        }
    }

    /**
     * 根据类型获取序列化器
     * @param clazz
     * @return
     */
    public static Serializer getSerializer(Class<?> clazz) {
        Serializer serializer = serializerMap.get(clazz);
        if (serializer == null) {
            // 如果获取不到，使用默认的序列化器
            serializer = new GenericSerializer();
        }
        return serializer;
    }

}
