package com.pansky.user.aspect.s1;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Fo
 * @date 2023/7/12 23:14
 */
public class DataMaskingSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private MaskTypeEnum maskTypeEnum;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(maskTypeEnum.function().apply(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        DataMask dataMask = property.getAnnotation(DataMask.class);
        if (Objects.nonNull(dataMask)&& Objects.equals(String.class, property.getType().getRawClass())) {
            this.maskTypeEnum = dataMask.function();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
