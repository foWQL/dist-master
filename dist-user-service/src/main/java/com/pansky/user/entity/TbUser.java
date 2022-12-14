package com.pansky.user.entity;

import java.io.Serializable;

/**
 * (TbUser)实体类
 *
 * @author Fo
 * @since 2022-11-12 18:04:35
 */
public class TbUser implements Serializable {
    private static final long serialVersionUID = -35620626899499314L;

    private Integer id;

    private String username;

    private String address;

    private Integer age;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}

