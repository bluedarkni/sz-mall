package com.shanzhen.common.utils.cipher;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * Description: 序列化Instant成毫秒
 * Created by nijunyang on 2020/2/19 23:08
 */
public class InstantJacksonSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(instant.toEpochMilli());
    }
}
