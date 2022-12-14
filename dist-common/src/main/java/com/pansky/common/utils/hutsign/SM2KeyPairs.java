package com.pansky.common.utils.hutsign;

/**
 * @author Fo
 * @date 2022/11/16 18:25
 */
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  SM2 国密算法生成秘钥对 - 对象
 */
@Data
@Accessors(chain = true)
//@ApiModel(value = "SM2KeyPairs", description = "SM2用户秘钥对对象")
public class SM2KeyPairs {
//    @ApiModelProperty(value = "公钥")
    private String publicKeyBase64;

//    @ApiModelProperty(value = "私钥")
    private String privateKeyBase64;
}
