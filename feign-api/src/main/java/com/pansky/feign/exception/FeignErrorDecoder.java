package com.pansky.feign.exception;

import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * @author Fo
 * @date 2022/10/19 12:11
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        try {

            String message = Util.toString(response.body().asReader(Charset.defaultCharset()));
            /*if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                return JsonUtil.parseObject(body, ApiException.class);
            }
            return new Exception(body);
            */

            JSONObject jsonObject = JSONObject.parseObject(message);
            // 包装成自己自定义的异常，这里建议根据自己的代码改
            return new ApiException(jsonObject.getString("message"), jsonObject.getInteger("code"));

        } catch (Exception e){
            return e;
        }
    }


}
