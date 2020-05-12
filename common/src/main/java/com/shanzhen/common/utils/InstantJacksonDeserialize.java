package com.shanzhen.common.utils.cipher;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

/**
 * Description:反序列毫秒成Instant
 * Created by nijunyang on 2020/2/19 23:05
 */
public class InstantJacksonDeserialize extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        Long aLong = Long.valueOf(text);
        Instant res = Instant.ofEpochMilli(aLong);
        return res;
    }
}
