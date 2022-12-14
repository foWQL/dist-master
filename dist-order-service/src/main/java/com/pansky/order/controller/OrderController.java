package com.pansky.order.controller;

import com.pansky.order.entiy.Order;
import com.pansky.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  Fo
 * @date 2022/7/12 22:36
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(HttpServletRequest request , HttpServletResponse response, @PathVariable("orderId") String orderId) {
        String filterHeader = request.getHeader("CL-Request");
        System.out.println(filterHeader);
        // 根据id查询订单并返回
        return orderService.queryOrderById(orderId);
    }

    @PostMapping("/updateOrder")
    public void updateOrder(String orderId, String userId){
        orderService.updateOrder(orderId,userId);
    }
}