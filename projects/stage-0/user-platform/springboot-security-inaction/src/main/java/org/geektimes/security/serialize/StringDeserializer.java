package org.geektimes.security.serialize;

import java.nio.charset.StandardCharsets;

public class StringDeserializer extends AbstractDeserializer<String> {

    @Override
    protected String doDeserialize(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
