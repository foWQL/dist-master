package com.pansky.common.utils.hutsign;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Fo
 * @date 2022/11/16 18:23
 */
@Slf4j
public class HutSignUtil {

    /**
     * 生成私钥 & 公钥
     *
     *
     * @return SM2KeyPairs对象
     */
    public static SM2KeyPairs getKeyParis() {
        SM2KeyPairs sm2KeyPairs = new SM2KeyPairs();
        //随机生成秘钥
        SM2 sm2 = new SM2();
        //获取秘钥对象
        PrivateKey privateKeyObject = sm2.getPrivateKey();
        PublicKey publicKeyObject = sm2.getPublicKey();
        //生成私钥
        String privateKeyBase64 = Base64.encode(privateKeyObject.getEncoded());
        //生成公钥
        String publicKeyBase64 = Base64.encode(publicKeyObject.getEncoded());
        sm2KeyPairs.setPrivateKeyBase64(privateKeyBase64);
        sm2KeyPairs.setPublicKeyBase64(publicKeyBase64);
        return sm2KeyPairs;
    }

    /**
     * SM2 文件加密
     *
     * @param publicKey     公钥
     * @param dataBytes     提交的原始文件以流的形式
     * @param outputPath    输出的加密文件路径
     * @param fileName      输出的加密文件名称
     */
    public static Boolean lockFile(String publicKey, byte[] dataBytes, String outputPath, String fileName) throws Exception {
        Boolean flag = false;
        if (StringUtils.isEmpty(publicKey) || null == dataBytes ||
                StringUtils.isEmpty(outputPath) || StringUtils.isEmpty(fileName)) {
            throw new Exception("缺少必要参数!");
        } else {
            long startTime = System.currentTimeMillis();
            // 初始化SM2对象
            try {
                SM2 SM_2 = new SM2(null, publicKey);
                byte[] data;
                data = SM_2.encrypt(dataBytes, KeyType.PublicKey);
                FileUtil.byteToFile(data, outputPath, fileName);
                flag = true;
            } catch (Exception e) {
                flag = false;
                log.error("Exception | " + e);
            }
            long endTime = System.currentTimeMillis();
            log.error("本次加密操作,所耗时间为：" + (endTime - startTime));
            return flag;
        }
    }

    /**
     * SM2 文件解密
     *
     * @param privateKey   私钥
     * @param lockFilePath 加密文件路径
     * @param outputPath   输出的解密文件路径
     * @param fileName     输出的解密文件名称
     */
    @SneakyThrows
    public static Boolean unlockFile(String privateKey, String lockFilePath, String outputPath, String fileName) {
        Boolean flag = false;
        if (StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(lockFilePath) || StringUtils.isEmpty(fileName)) {
            throw new Exception("缺少必要参数!");
        } else {
            long startTime = System.currentTimeMillis();
            try {
                // 初始化SM2对象
                SM2 SM_2 = new SM2(privateKey, null);
                byte[] bytes = FileUtil.fileToByte(lockFilePath);
                byte[] data;
                data = SM_2.decrypt(bytes, KeyType.PrivateKey);
                FileUtil.byteToFile(data, outputPath, fileName);
                flag = true;
            } catch (Exception e) {
                log.error("Exception | " + e);
            }
            long endTime = System.currentTimeMillis();
            log.error("本次解密操作,所耗时间为：" + (endTime - startTime));
            return flag;
        }
    }

    /**
     * 通过私钥进行文件签名
     *
     * @param privateKey    私钥
     * @param dataBytes 需要签名的文件以流的形式
     * @throws Exception
     */
    public static String generateFileSignByPrivateKey(String privateKey, byte[] dataBytes) throws Exception {
        String signature = "";
        if (StringUtils.isEmpty(privateKey) || null == dataBytes) {
            throw new Exception("缺少必要参数!");
        } else {
            long startTime = System.currentTimeMillis();
            String signs = "";
            try {
                //----------------------20210830优化:私钥HEX处理---------------------------------
                byte[] decode = Base64.decode(privateKey);
                SM2 sm3 = new SM2(decode,null);
                byte[] bytes = BCUtil.encodeECPrivateKey(sm3.getPrivateKey());
                String privateKeyHex = HexUtil.encodeHexStr(bytes);
                //------@End----------------20210830优化:私钥HEX处理------------------------------
                //需要加密的明文,得到明文对应的字节数组
                ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKeyHex);
                //创建sm2 对象
                SM2 sm2 = new SM2(privateKeyParameters, null);
                //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
                sm2.usePlainEncoding();
                sm2.setMode(SM2Engine.Mode.C1C2C3);
                byte[] sign = sm2.sign(dataBytes, null);
                //change encoding : hex to base64
                signs = Base64.encode(sign);
                signature = signs;
            } catch (Exception e) {
                log.error("Exception | " + e);
            }
            long endTime = System.currentTimeMillis();
            log.error("本次签名操作,所得签名为:" + signs + ",所耗时间为：" + (endTime - startTime));
            return signature;
        }
    }

    /**
     * 通过公钥进行文件验签
     *
     * @param publicKey 公钥
     * @param sign 签名（原先为hex处理后的16位，现在改为base处理后的64位）
     * @param dataBytes 需要验签的文件数据以流的形式
     * @return
     * @throws Exception
     */
    public static Boolean verifyFileSignByPublicKey(String publicKey, String sign, byte[] dataBytes) throws Exception {
        if (StringUtils.isEmpty(publicKey) || StringUtils.isEmpty(sign) || null == dataBytes) {
            throw new Exception("缺少必要参数!");
        } else {
            long startTime = System.currentTimeMillis();
            Boolean verify = false;
            try {
                //-----------------------------20210830修改公钥HEX处理----------------------------
                byte[] decode = Base64.decode(publicKey);
                SM2 sm3 = new SM2(null, decode);
                byte[] bytes = ((BCECPublicKey) sm3.getPublicKey()).getQ().getEncoded(false);
                String publicKeyHex = HexUtil.encodeHexStr(bytes);
                //--------@End---------------------公钥HEX处理------------------------------------
                //需要加密的明文,得到明文对应的字节数组
                //这里需要根据公钥的长度进行加工
                if (publicKeyHex.length() == 130) {
                    //这里需要去掉开始第一个字节 第一个字节表示标记
                    publicKeyHex = publicKeyHex.substring(2);
                }
                String xhex = publicKeyHex.substring(0, 64);
                String yhex = publicKeyHex.substring(64, 128);
                ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
                //创建sm2 对象
                SM2 sm2 = new SM2(null, ecPublicKeyParameters);
                //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
                sm2.usePlainEncoding();
                sm2.setMode(SM2Engine.Mode.C1C2C3);
                verify = sm2.verify(dataBytes, Base64.decode(sign));
            } catch (Exception e) {
                log.error("Exception | " + e);
            }
            long endTime = System.currentTimeMillis();
            log.error("本次验签操作,所得结果为:" + verify + ",所耗时间为：" + (endTime - startTime));
            return verify;
        }
    }
}
