package com.pansky.user.entity;

//import com.pansky.user.aspect.s2.Sensitive;

import com.pansky.user.aspect.s2.Sensitive;

/**
 * @author Fo
 * @date 2023/7/13 0:42
 */
public class Student {

    @Sensitive(type = Sensitive.Type.CHINESE_NAME)
    private String name;

    @Sensitive(type = Sensitive.Type.ID_CARD)
    private String idCard;

    @Sensitive(type = Sensitive.Type.BANK_CARD)
    private String bankCard;

    @Sensitive(type = Sensitive.Type.FIXED_PHONE)
    private String fixedPhone;

    @Sensitive(type = Sensitive.Type.ADDRESS)
    private String address;

    @Sensitive(type = Sensitive.Type.EMAIL)
    private String email;

    @Sensitive(type = Sensitive.Type.CUSTOM_RETAIN_HIDE,startInclude = 3,endExclude = 10)
    private String remark;

    public Student() {
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Student(String name, String idCard, String bankCard, String fixedPhone, String address, String email, String remark) {
        this.name = name;
        this.idCard = idCard;
        this.bankCard = bankCard;
        this.fixedPhone = fixedPhone;
        this.address = address;
        this.email = email;
        this.remark = remark;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}