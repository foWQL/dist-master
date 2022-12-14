package com.pansky.order.mapper;

import com.pansky.order.entiy.Order;
import org.apache.ibatis.annotations.Select;

/**
 * @author
 * @date 2022/7/12 22:33
 */
public interface OrderMapper {

    @Select("select * from tb_order where id = #{id}")
    Order findById(String id);

    @Select("update tb_order set price = #{price} where  id = #{id}")
    void updateOrder(String id, int price);

}
