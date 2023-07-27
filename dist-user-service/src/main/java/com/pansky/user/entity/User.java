package com.pansky.user.entity;

import com.pansky.user.aspect.s1.DataMask;
import com.pansky.user.aspect.s1.MaskTypeEnum;
import lombok.Data;

/**
 * @author  Fo
 * @date 2022/7/12 22:23
 */
@Data
public class User {
    private String id;
    @DataMask(function = MaskTypeEnum.USERNAME)
    private String username;
//    @DataMask(function = MaskTypeEnum.USERNAME)
    private String address;
    @DataMask(function = MaskTypeEnum.PHONE)
    private int age;
}
