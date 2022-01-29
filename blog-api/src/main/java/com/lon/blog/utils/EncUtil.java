package com.lon.blog.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class EncUtil {

    // TODO 配在数据库里
    private static final String salt = "lon!@#";

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
