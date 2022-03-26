package com.lon.blog.utils;

import com.lon.blog.service.ConfigService;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密
 * TODO 换成其它加密算法
 */
public class EncUtil {
    //加密盐
    private static  String salt;

    private static ConfigService configService = SpringContextUtil.getBean(ConfigService.class);

    static {
        salt = configService.findValueByName("encSalt");
    }

    public static String encrpty(String data){
        String ciphertext = DigestUtils.md5Hex(data + salt);
        return ciphertext ;
    }

    public static void main(String[] args) {
        String str = encrpty("365694lon");
        String str2 = encrpty("admin");

        System.out.println(str);
        System.out.println(str2);
    }

}
