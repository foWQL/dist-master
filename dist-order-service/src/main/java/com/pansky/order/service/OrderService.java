package com.pansky.order.service;

import com.pansky.feign.clients.UserClient;
import com.pansky.feign.entiy.User;
import com.pansky.order.entiy.Order;
import com.pansky.order.mapper.OrderMapper;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author  Fo
 * @date 2022/7/12 22:34
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(String orderId) {
        //1. 查询订单
        Order order = orderMapper.findById(orderId);
        //2. 查询用户
        User user = userClient.findById(order.getUserId());
        order.setUser(user);
        return order;
    }

    @GlobalTransactional(name = "my_test_tx_group",rollbackFor = Exception.class)
    public void updateOrder(String orderId, String userId){
        log.info("order服务中全局事务id=================>{}", RootContext.getXID());
        //1. 更新 order 服务
        orderMapper.updateOrder(orderId, 888);
        //2. 更新 user 服务
        userClient.updateUser(userId,1);
     }
}
