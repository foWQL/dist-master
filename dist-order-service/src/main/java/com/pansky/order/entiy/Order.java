package com.pansky.order.entiy;

import com.pansky.feign.entiy.User;
import lombok.Data;

/**
 * @author  Fo
 * @date 2022/7/12 22:33
 */
@Data
public class Order {
    private String id;
    private Long price;
    private String name;
    private Integer num;
    private String userId;
    private User user;
}
