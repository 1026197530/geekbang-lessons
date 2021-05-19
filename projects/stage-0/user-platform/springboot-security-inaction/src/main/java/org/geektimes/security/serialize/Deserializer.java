package org.geektimes.security.serialize;

public interface Deserializer<T> {

    T deserialize(byte[] bytes);

}
