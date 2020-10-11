package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.unit.CookieUtils;
import com.jt.vo.SysResult;
import net.sf.jsqlparser.schema.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user")
@Controller//如果返回的是json加@Responsebody
public class UserController {

    //消费者启动时是否校验有提供者 false不检查 启动时候检查
    @Reference(check = false)
    private DubboUserService userService;
    private static final String TICKET="JT_TICKET";
    @Autowired
    private JedisCluster jedisCluster;
    /**
     * 要求用一个方法实现页面的通用跳转
     * http://www.jt.com/usr/login.html 跳转login
     * http://www.jt.com/usr/register.html 跳转register
     */
    @RequestMapping("/{moduleName}")
    public String doUser(@PathVariable String moduleName){

        return moduleName;
    }

    /**
     * 完成数据的入库操作
     * http://www.jt.com/usr/doRegister
     * 参数问题 password:_password,username:_username,phone:_phone
     * 返回值结果 一个sysResult对象
     */

    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult doRegister(User user){
        userService.saveUser(user);
        return SysResult.success();

    }
    /**
     * 1url：http://www.jt.com/usr/doLogin
     * 参数  用户名和密码 username password
     * 返回值结果： SysResult
     * 需求 ：将cookie名称为jt_ticket的数据输出到浏览器中，7天超时 实现jt.com数据共享
     *
     * cookie的特点：
     *     1：只能查看当前网址下的cookie信息
     *     2： Domain表示cookie的共享策略 实现数据的共享 domain www。jd。com 数据只能在当前的域名中使用
     *          jd。com 可以在以域名为jd。com结尾的域名中实现共享
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        //完成用户的登陆操作 获取ticket信息
        String ticket =userService.doLogin(user);
        if (StringUtils.isEmpty(ticket)){
            //如果为空则说明账号或密码有问题
            return SysResult.fail();
        }
        //1创建cookie对象
        Cookie cookie=new Cookie("JT_TICKET",ticket);
      /* 2 设置最大时间 value=-1表示关闭回话是cookie删除
        设置最大时间 value=0表示cookie立刻删除
        设置最大时间 value>0表示设置cookie超时时间*/
        cookie.setMaxAge(7*24*60*60);
        cookie.setDomain("jt.com");
        //www.jt.com/aaa/bb/index
        cookie.setPath("/");
        //3 将数据保存到浏览器中
        response.addCookie(cookie);
        return SysResult.success();
    }
    /**
     * 实现用户的推出操作
     * url：http：//www.jt.com/user/logout.html
     * 返回值 重定向到系统首页
     * 目的  删除cookie 和redis
     * 前提  需要获取cookie的key和他的value
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //1,如何获取cookie中的数据？
        Cookie cookie = CookieUtils.getCookieByName(request, TICKET);
        //2 校验cookie的数据是否为空
        if (cookie!=null){
            String jtTicket = cookie.getValue();
            if (!StringUtils.isEmpty(jtTicket)){
                jedisCluster.del(jtTicket);
                CookieUtils.deleteCookie(TICKET, "/", "com.jt", response);
            }
        }
        return "redirect:/";
    }










}
