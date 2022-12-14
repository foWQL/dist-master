package com.pansky.order.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.pansky.common.utils.SignSecretUtil;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  Fo
 * @date 2022/7/12 22:36
 */
@RestController
@RequestMapping("/third")
public class ThirdController {

    @PostMapping("/apply")
    public void queryOrderByUserId() {

        String auth = "SPL";

        // 方法一 使用Hutool的加密验签  多种加密可选
        try {

            KeyPair pair = SecureUtil.generateKeyPair("RSA");
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            // 将公钥私钥转换成 Base64 字符串
            String privateKeyStr = Base64.encode(privateKey.getEncoded());
            String publicKeyStr = Base64.encode(publicKey.getEncoded());


//            PrivateKey privateKey = SecureUtil.generatePrivateKey("RSA", Base64.decode(privateKeyStr));
            Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
            // 设置私钥，然后执行签名
            sign.setPrivateKey(privateKey);
            byte[] signedData = sign.sign(auth.getBytes());
            String signStr = Base64.encode(signedData);

            System.out.println("publicKeyStr = " + publicKeyStr);
            
            Map<String, String> headerMap = new HashMap<String,String>();
            headerMap.put("sign", signStr);
            headerMap.put("publicKeyStr", publicKeyStr);

            HttpRequest.get("http://localhost:8080/user/101").addHeaders(headerMap).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }


        // 方法二 使用自定义的加密验签类 SignSecretUtil
        /*try {
            KeyPair keyPair = SignSecretUtil.getKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
            System.out.println("publicKeyStr = " + publicKeyStr);

            String sign = SignSecretUtil.sign(auth, privateKey);
            System.out.println("sign = " + sign);

            Map<String, String> headerMap = new HashMap<String,String>();
            headerMap.put("sign", sign);
            headerMap.put("publicKeyStr", publicKeyStr);

            Sign sign1 = SecureUtil.sign(SignAlgorithm.MD5withRSA);

            HttpRequest.get("http://localhost:8080/user/101").addHeaders(headerMap).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }

}