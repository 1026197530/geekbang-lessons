package org.geektimes.security.serialize;

import java.nio.charset.StandardCharsets;

public class StringSerializer extends AbstractSerializer<String> {

    @Override
    public byte[] doSerialize(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

}
