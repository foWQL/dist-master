package com.pansky.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.List;

/**
 * @author Fo
 * @date 2022/8/7 11:43
 */

//@Order(-1)
//@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 网关基于 WebFlux 响应式编程编写，api可能有些不一样
         ServerHttpRequest request = exchange.getRequest();

        HttpHeaders headers = request.getHeaders();
        List<String> list = headers.get("CL-Request");  //获取不到配置的filter，需要通过HttpServletRequest获取

        //基于参数中用户（admin）的校验
        // 1. 获取参数中的authorization 参数
        MultiValueMap<String, String> params = request.getQueryParams();
        // 2， 判断参数值是否等于 admin
        String auth = params.getFirst("authorization");

        // 3. 如果相等，放行  到下一个过滤器
        if ("admin".equals(auth)) {
            return chain.filter(exchange);
        }

        // 响应相关的数据
        ServerHttpResponse response = exchange.getResponse();
        // HttpServletRequest  这个是web里面的
        // ServerHttpRequest   webFlux响应式里面的

        response.getHeaders().set("content-type","application/json;charset=utf-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        HashMap<String, Object> map = new HashMap<>(4);
        map.put("code", 430);
        map.put("msg","你不是admin，权限不足");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 通过buffer工厂将字节数组包装成 一个数据包
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(wrap));

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
