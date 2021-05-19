package org.geektimes.security.serialize;

import java.nio.charset.StandardCharsets;

public class LongSerializer extends AbstractSerializer<Long> {

    @Override
    public byte[] doSerialize(Long value) {
        return value.toString().getBytes(StandardCharsets.UTF_8);
    }

}
