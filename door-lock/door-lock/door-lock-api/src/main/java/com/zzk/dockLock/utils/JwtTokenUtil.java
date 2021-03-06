package com.zzk.dockLock.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @description: JwtToken工具类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/07 16:42
 */
@Component
public class JwtTokenUtil {


    //Claims里的第二个参数
    private static final String CLAIM_KEY_USERNAME = "sub";
    //荷载里面的创建时间
    private static final String CLAIM_KEY_CREATED = "created";

    //加密密钥
    @Value ("${jwt.secret}")
    private String secret;
    //过期时间
    @Value ("${jwt.expiration}")
    private Long expiration;


    /**
     * @Description: 根据用户信息生成Token
     * @Title: generateToken
     * @param userDetails
     * @return: java.lang.String
     * @author: zzk
     * @Date: 2021/11/7 16:58
     */
    public String generateToken(UserDetails userDetails){
        Map<String ,Object> claims=new HashMap<>();
        //获取用户名
        claims.put (CLAIM_KEY_USERNAME,userDetails.getUsername ());
        //获取当前时间
        claims.put (CLAIM_KEY_CREATED,new Date ());
        return generateToken(claims);

    }


    /**
     * @Description: 根据荷载生成需要的JwtToken
     * @Title: genetareToken
     * @param claims
     * @return: java.lang.String
     * @author: zzk
     * @Date: 2021/11/7 17:13
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder ()
                //设置载荷
                .setClaims (claims)
                //设置过期时间
                .setExpiration (genetareExpirationDate())
                //设置加密算法和密钥
                .signWith (SignatureAlgorithm.HS512,secret)
                .compact ();
    }

    /**
     * @Description:  生成token失效时间
     * @Title: genetareExpirationDate
     * @param
     * @return: java.util.Date
     * @author: zzk
     * @Date: 2021/11/7 17:19
     */
    private Date genetareExpirationDate() {
        return new Date (System.currentTimeMillis ()+expiration*1000);
    }

    /**
     * @Description: 验证token是否有效
     * @Title: validateToken
     * @param token  @param userDetails
     * @return: boolean
     * @author: zzk
     * @Date: 2021/11/7 17:30
     */
    public boolean validateToken(String token,UserDetails userDetails){

        String username=getUserNameFromToken(token);
        return username.equals (userDetails.getUsername ()) && !isTokenExpired(token);

    }


    /**
     * @Description: 判断token是否失效
     * @Title: isTokenExpired
     * @param token
     * @return: boolean
     * @author: zzk
     * @Date: 2021/11/7 19:25
     */
    private boolean isTokenExpired(String token) {
       Date expiredDate=getExpiredDateFromToken(token);
       return expiredDate.before (new Date ());

    }

    /**
     * @Description: 从token中获取过期时间
     * @Title: getExpiredDateFromToken
     * @param token
     * @return: java.util.Date
     * @author: zzk
     * @Date: 2021/11/7 19:27
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken (token);
        return claims.getExpiration ();

    }

    /**
     * @Description: 从token中获取用户名
     * @Title: getUserNameFromToken
     * @param token
     * @return: java.lang.String
     * @author: zzk
     * @Date: 2021/11/7 17:35
     */
    public String getUserNameFromToken(String token) {
       String username;

        try {
            Claims claims=getClaimsFromToken(token);
            username = claims.getSubject ();
        } catch (Exception e) {
            username=null;
        }
        return username;
    }

    /**
     * @Description: 从token获取载荷（playload）
     * @Title: getClaimsFromToken
     * @param token
     * @return: io.jsonwebtoken.Claims
     * @author: zzk
     * @Date: 2021/11/7 17:40
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims=null;

        try {
            claims=Jwts.parser ()
                    //设置密钥（签名）
                    .setSigningKey (secret)
                    .parseClaimsJws (token)
                    .getBody ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return claims;
    }


    /**
     * @Description: 判断是token是否可以被刷新
     * @Title: canRefresh
     * @param token
     * @return: boolean
     * @author: zzk
     * @Date: 2021/11/7 19:39
     */
    public boolean canRefresh(String token){
        return !isTokenExpired (token);
    }


    /**
     * @Description: 刷新token
     * @Title: refreshToken
     * @param token
     * @return: java.lang.String
     * @author: zzk
     * @Date: 2021/11/7 19:37
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken (token);
        claims.put (CLAIM_KEY_CREATED,new Date ());
        return generateToken (claims);

    }

    /**
     * 生成测试密码
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        System.out.println ("请输入你要加密的密码：");
        String encode = passwordEncoder.encode (sc.nextLine ());
        System.out.println (encode);


    }


}