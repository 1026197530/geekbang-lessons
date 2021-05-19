package org.geektimes.security.serialize;

public abstract class AbstractDeserializer<T> implements Deserializer<T> {

    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return doDeserialize(bytes);
    }

    /**
     * 执行反序列化操作
     * @param bytes 不为null, 且不为空字节数组
     * @return
     */
    protected abstract T doDeserialize(byte[] bytes);

}
