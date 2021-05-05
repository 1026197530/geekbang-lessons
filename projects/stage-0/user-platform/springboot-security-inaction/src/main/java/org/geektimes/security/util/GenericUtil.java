package org.geektimes.security.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 */
public class GenericUtil {

    private GenericUtil() {
    }

    /**
     * 获取类继承的父类的泛型
     * <tt>public class LongSerializer extends AbstractSerializer<Long>{}</tt>
     * @param clazz
     * @param index
     * @return
     */
    public static Type getClassGenericType(Class<?> clazz, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return actualTypeArguments[index];
    }

    /**
     * 获取类实现的第一个接口上的泛型
     * @param clazz 不要传lambda表达式
     * @param index
     * @return
     */
    public static Type getInterfaceGenericType(Class<?> clazz, int index) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return actualTypeArguments[index];
    }

}
