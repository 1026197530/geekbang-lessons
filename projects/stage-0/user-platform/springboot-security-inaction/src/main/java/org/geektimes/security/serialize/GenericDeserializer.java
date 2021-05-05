package org.geektimes.security.serialize;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class GenericDeserializer<T> extends AbstractDeserializer<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T doDeserialize(byte[] bytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
