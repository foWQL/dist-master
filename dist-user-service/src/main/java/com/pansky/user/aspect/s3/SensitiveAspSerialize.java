package com.pansky.user.aspect.s3;

/**
 * @author Fo
 * @date 2023/7/13 0:48
 */
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>
 * 脱敏序列化
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveAspSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum type;

    private Integer prefixNoMaskLen;

    private Integer suffixNoMaskLen;

    private String maskStr;

    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        switch (type) {
            case CHINESE_NAME:
                jsonGenerator.writeString(DesensitizedUtils.chineseName(origin));
                break;
            case ID_CARD:
                jsonGenerator.writeString(DesensitizedUtils.idCardNum(origin));
                break;
            case FIXED_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.fixedPhone(origin));
                break;
            case MOBILE_PHONE:
                jsonGenerator.writeString(DesensitizedUtils.mobilePhone(origin));
                break;
            case ADDRESS:
                jsonGenerator.writeString(DesensitizedUtils.address(origin));
                break;
            case EMAIL:
                jsonGenerator.writeString(DesensitizedUtils.email(origin));
                break;
            case BANK_CARD:
                jsonGenerator.writeString(DesensitizedUtils.bankCard(origin));
                break;
            case PASSWORD:
                jsonGenerator.writeString(DesensitizedUtils.password(origin));
                break;
            case KEY:
                jsonGenerator.writeString(DesensitizedUtils.key(origin));
                break;
            case CUSTOMER:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, maskStr));
                break;
            default:
                throw new IllegalArgumentException("Unknow sensitive type enum " + type);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                SensitiveAsp sensitive = beanProperty.getAnnotation(SensitiveAsp.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(SensitiveAsp.class);
                }
                if (sensitive != null) {
                    return new SensitiveAspSerialize(sensitive.type(), sensitive.prefixNoMaskLen(),
                            sensitive.suffixNoMaskLen(), sensitive.maskStr());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }

}