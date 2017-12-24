package com.ew.udm.configs;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class LongValueSerializer implements ObjectSerializer {
    private final static long JS_LONG_MAX = 0x000fffffffffffffL;
    @Override
    public void write(JSONSerializer jsonSerializer, Object object, Object fieldName, Type type, int features) throws IOException {
        SerializeWriter out = jsonSerializer.out;
        Long value = (Long) object;
        if (Math.abs(value) > JS_LONG_MAX) {
            out.writeString(value.toString());
        } else {
            out.writeLong(value);
        }
    }
}
