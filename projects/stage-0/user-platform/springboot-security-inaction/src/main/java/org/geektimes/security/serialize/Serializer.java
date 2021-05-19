package org.geektimes.security.serialize;

public interface Serializer<T> {

    byte[] serialize(T value);

}
