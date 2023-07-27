package com.pansky.user.aspect.s3;

/**
 * @author Fo
 * @date 2023/7/13 0:45
 */
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//https://blog.csdn.net/weixin_44866272/article/details/119905374
//

/**
 * 对象脱敏注解
 *
 * @version v1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveAspSerialize.class)
public @interface SensitiveAsp {

    /**
     * 脱敏数据类型, 非Customer时, 将忽略 refixNoMaskLen 和 suffixNoMaskLen 和 maskStr
     */
    SensitiveTypeEnum type() default SensitiveTypeEnum.CUSTOMER;

    /**
     * 前置不需要打码的长度
     */
    int prefixNoMaskLen() default 0;

    /**
     * 后置不需要打码的长度
     */
    int suffixNoMaskLen() default 0;

    /**
     * 用什么打码
     */
    String maskStr() default "*";

}
