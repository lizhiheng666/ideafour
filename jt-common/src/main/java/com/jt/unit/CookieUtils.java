package com.jt.unit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    //新增cookie
    public static void addCookie(String name, String value,
                                 String path, int maxAge,
                                 String domain, HttpServletResponse response){
        Cookie cookie=new Cookie(name, value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

    }
    //删除cookie
    public static void deleteCookie(String name, String path,
                                    String domain,
                                    HttpServletResponse response){

        Cookie cookie=new Cookie(name, "");
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    //根据cookie的name获取cookie的属性
    public  static Cookie getCookieByName(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                if (name.equalsIgnoreCase(cookie.getName())){
                    return cookie;
                }
            }
        }
        return null;
    }








}
