package com.pansky.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Stu)实体类
 *
 */
@EqualsAndHashCode
@Data
@AllArgsConstructor
public class Stu implements Serializable {
    private static final long serialVersionUID = -48507194368189008L;
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 学生名字
     */
    private String name;
    /**
     * 学校名字
     */
    private String school;
    /**
     * 学生小名
     */
    private String nickname;
    /**
     * 学生年龄
     */
    private Integer age;
    /**
     * 班级人数
     */
    private Integer classNum;
    /**
     * 成绩
     */
    private Double score;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 家庭网络邮箱
     */
    private String email;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 家庭地址
     */
    private String address;

    public Stu(String name, String nickname, String school,Integer age, Integer classNum, Double score, String phone, String email, String ip, String address) {
        this.name = name;
        this.school = school;
        this.nickname = nickname;
        this.age = age;
        this.classNum = classNum;
        this.score = score;
        this.phone = phone;
        this.email = email;
        this.ip = ip;
        this.address = address;
    }
}

