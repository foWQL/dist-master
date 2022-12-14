package com.pansky.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pansky.common.utils.SignSecretUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;

/**
 * @author Fo
 * @date 2022/8/7 11:43
 */

//@Order(-1)
@Component
public class PanAuthorizeFilter implements GlobalFilter, Ordered {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String data = "SPL";

        // 网关基于 WebFlux 响应式编程编写，api可能有些不一样
         ServerHttpRequest request = exchange.getRequest();

        HttpHeaders headers = request.getHeaders();
        String signStr = headers.getFirst("sign");
        String publicKeyStr = headers.getFirst("publicKeyStr");
//        byte[] bytes = auth.getBytes();

        PublicKey publicKey = null;

        // 方法一 使用Hutool的验签
        try {
            Sign sign = SignUtil.sign(SignAlgorithm.MD5withRSA);
            publicKey = SecureUtil.generatePublicKey("RSA", Base64.decode(publicKeyStr));
            sign.setPublicKey(publicKey);
            // 将签名字符串转换成 byte 数组
            byte[] bytes = Base64.decode(signStr);
            //验证签名
            boolean flag = sign.verify(data.getBytes(), bytes);
//            publicKey = SignSecretUtil.getPublicKey(publicKeyStr);
            // 3. 如果相等，放行  到下一个过滤器
            if (flag) {
                return chain.filter(exchange);
            }else {
                // 响应相关的数据
                ServerHttpResponse response = exchange.getResponse();
                // HttpServletRequest  这个是web里面的
                // ServerHttpRequest   webFlux响应式里面的

                response.getHeaders().set("content-type","application/json;charset=utf-8");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                HashMap<String, Object> map = new HashMap<>(4);
                map.put("code", 430);
                map.put("msg","签名验证不通过");
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] res = new byte[0];
                res = objectMapper.writeValueAsBytes(map);
                // 通过buffer工厂将字节数组包装成 一个数据包
                DataBuffer wrap = response.bufferFactory().wrap(res);
                return response.writeWith(Mono.just(wrap));
            }
        } catch (Exception e) {
            System.out.println("sssssssss");
            e.printStackTrace();
            return null;
        }

        // 方法二 使用自定义的加密验签类 SignSecretUtil
        /*try {
            publicKey = SignSecretUtil.getPublicKey(publicKeyStr);
            //验证签名
            boolean flag = SignSecretUtil.verify(data,publicKey,sign);

            // 3. 如果相等，放行  到下一个过滤器
            if (flag) {
                return chain.filter(exchange);
            }else {
                // 响应相关的数据
                ServerHttpResponse response = exchange.getResponse();
                // HttpServletRequest  这个是web里面的
                // ServerHttpRequest   webFlux响应式里面的

                response.getHeaders().set("content-type","application/json;charset=utf-8");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                HashMap<String, Object> map = new HashMap<>(4);
                map.put("code", 430);
                map.put("msg","签名验证不通过");
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] res = new byte[0];
                res = objectMapper.writeValueAsBytes(map);
                // 通过buffer工厂将字节数组包装成 一个数据包
                DataBuffer wrap = response.bufferFactory().wrap(res);
                return response.writeWith(Mono.just(wrap));
            }
        } catch (Exception e) {
            System.out.println("sssssssss");
            e.printStackTrace();
            return null;
        }*/

    }

    /**
     * 指定顺序  最好在0附近  -2 -1 0 1
     * 越小越先执行
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
