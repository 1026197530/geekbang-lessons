package org.geektimes.security.serialize;

import java.nio.charset.StandardCharsets;

public class LongDeserializer extends AbstractDeserializer<Long> {

    @Override
    protected Long doDeserialize(byte[] bytes) {
        return Long.parseLong(new String(bytes, StandardCharsets.UTF_8));
    }

}
