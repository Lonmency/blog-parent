package com.lon.blog.utils;

import com.lon.blog.service.ConfigService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtils {

    //密钥
    private static  String jwtTokenKey ;

    private static ConfigService configService = SpringContextUtil.getBean(ConfigService.class);

    static{
        jwtTokenKey = configService.findValueByName("jwtTokenKey");
    }

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);    //B
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtTokenKey) // 签发算法，秘钥为jwtToken A
                .setClaims(claims) // body数据，要唯一，自行设置 playload B
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));// 一天的有效时间
        //计算token
        String token = jwtBuilder.compact();
        return token;
    }

    //解析token
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtTokenKey).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            log.error("验证token失败: " + e.getMessage() , e);
        }
        return null;

    }

    public static void main(String[] args) {
        String token = JWTUtils.createToken(100L);
        System.out.println(token);
        Map<String, Object> map = JWTUtils.checkToken(token);
        System.out.println(map.get("userId"));
    }

}
