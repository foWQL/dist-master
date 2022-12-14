package com.pansky.user.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class Tea implements Serializable {
    private static final long serialVersionUID = -25964094864107663L;
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 学校
     */
    private String school;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 班级
     */
    private Integer classNum;
    /**
     * 课程
     */
    private Integer sourceId;
    /**
     * 电话
     */
    private String phone;


    public Tea(String name, String school, Integer age, Integer classNum, Integer sourceId, String phone) {
        this.name = name;
        this.school = school;
        this.age = age;
        this.classNum = classNum;
        this.sourceId = sourceId;
        this.phone = phone;
    }


}

