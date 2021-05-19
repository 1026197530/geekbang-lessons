package org.geektimes.security.serialize;

public abstract class AbstractSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(T value) {
        if (value == null) {
            return new byte[0];
        }
        return doSerialize(value);
    }

    /**
     * 执行序列化操作
     * @param value 不为null
     * @return
     */
    protected abstract byte[] doSerialize(T value);

}
