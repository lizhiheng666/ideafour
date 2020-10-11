package com.jt.handler;

import com.jt.pojo.User;
import com.jt.unit.CookieUtils;
import com.jt.unit.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//拦截器的类 （业务）  拦截器的配置文件（拦截什么请求）
@Component
public class UserInterceptor implements HandlerInterceptor {
    /**实现pre的方法
     * return false 表示拦截 需要配合重定向一起使用  true表示放行
     * 需求：用户未登录 重定向到登陆界面
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    private static final String TICKET="JT_TICKET";
    private static final String JTUSER="JT_USER";
    @Autowired
    private JedisCluster jedisCluster;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie= CookieUtils.getCookieByName(request, TICKET);
        if (cookie!=null){
            String ticket=cookie.getValue();
            if (jedisCluster.exists(ticket)){
                String userJSON=jedisCluster.get(ticket);
                User user= ObjectMapperUtil.toObject(userJSON, User.class);
                request.setAttribute(JTUSER, user);
                //利用threadlocal方式存取数据
                UserThreadLocal.set(user);
                return true;

            }else {
                CookieUtils.deleteCookie(TICKET, "/", "jt.com", response);

            }
        }
        response.sendRedirect("/user/login.html");
        return false;
    }
    //实现数据的移除

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
