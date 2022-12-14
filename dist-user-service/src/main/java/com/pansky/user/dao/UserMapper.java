package com.pansky.user.dao;

import com.pansky.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author
 * @date 2022/7/12 22:23
 */
public interface UserMapper {

    @Select("select * from tb_user where id = #{id}")
    User findById(@Param("id") String id);

    @Update("update tb_user set age = #{age} where id = #{id}")
    void updateUser(@Param("id") String id , @Param("age") int age);

    @Insert("insert into tb_user values (#{id},#{username},#{address},#{age})")
    void insertUser(@Param("id") String id ,@Param("username") String username,@Param("address") String address, @Param("age") int age);

}
