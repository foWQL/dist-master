package com.pansky.user.aspect.s2;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import jodd.util.StringPool;

import java.io.IOException;
import java.util.Objects;


/**
 * @author Fo
 * @date 2023/7/13 0:41
 */

public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private Sensitive.Type type;
    private int startInclude;
    private int endExclude;
    private int overlayRepeat;

    public SensitiveSerializer() {}
    public SensitiveSerializer(final Sensitive sensitive) {
        this.type = sensitive.type();
        this.startInclude = sensitive.startInclude();
        this.endExclude = sensitive.endExclude();
        this.overlayRepeat = sensitive.overlayRepeat();
    }


    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(ObjectUtil.isEmpty(value)){
            gen.writeString(value);
            return;
        }else {
            switch (this.type) {
                case MOBILE:
                    gen.writeString(SensitiveUtil.handlerMobile(value));
                    break;
                case ID_CARD:
                    gen.writeString(SensitiveUtil.handlerIdCard(value));
                    break;
                case BANK_CARD:
                    gen.writeString(SensitiveUtil.handlerBankCard(value));
                    break;
                case CHINESE_NAME:
                    gen.writeString(SensitiveUtil.handlerUsername(value));
                    break;
                case FIXED_PHONE:
                    gen.writeString(SensitiveUtil.handlerFixedPhone(value));
                    break;
                case ADDRESS:
                    gen.writeString(SensitiveUtil.handlerAddress(value));
                    break;
                case EMAIL:
                    gen.writeString(SensitiveUtil.handlerEmail(value));
                    break;
                case CUSTOM_HIDE:
                    gen.writeString(SensitiveUtil.hide(value,startInclude,endExclude));
                    break;
                case CUSTOM_RETAIN_HIDE:
                    gen.writeString(SensitiveUtil.hide(value,startInclude,(value.length()-endExclude)));
                    break;
                case CUSTOM_OVERLAY:
                    gen.writeString(SensitiveUtil.overlay(value, StringPool.ASTERISK,overlayRepeat,startInclude,endExclude));
                    break;
                default:
                    gen.writeString(value);
            }
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if(Objects.isNull(property)){
            return prov.getDefaultNullValueSerializer();
        }
        if(Objects.equals(property.getType().getRawClass(), String.class)){
            Sensitive sensitive = property.getAnnotation(Sensitive.class);
            if (Objects.isNull(sensitive)) {
                sensitive = property.getContextAnnotation(Sensitive.class);
            }
            if (Objects.nonNull(sensitive)) {
                return new SensitiveSerializer(sensitive);
            }
        }
        return prov.findValueSerializer(property.getType(), property);
        //Sensitive sensitive = property.getAnnotation(Sensitive.class);
        //type = sensitive.value();
        //return this;
    }
}

