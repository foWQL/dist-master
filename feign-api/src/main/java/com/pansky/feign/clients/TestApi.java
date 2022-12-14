package com.pansky.feign.clients;


import com.pansky.feign.entiy.SysEx;
import com.pansky.feign.exception.FeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "service-a", contextId = "service-client-a", url = "${feign.service.url}", path = "/test")
@FeignClient(name = "userService", contextId = "userService2",fallback = FeignErrorDecoder.class)
public interface TestApi {
    @GetMapping("/no-ex")
    String noThrowsEx();

    @GetMapping("/has-ex")
    String hasThrowsEx(@RequestParam Integer code) throws SysEx;
}

