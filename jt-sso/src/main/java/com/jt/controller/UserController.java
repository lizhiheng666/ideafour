package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.Service.UserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;
    @RequestMapping("/getmsg")
    public String getmsg(){
        return "这是一个愉快的下午";
    }
    /**
     * 1url的請求地址/sso.jt.com/user/check/{param}/{type}
     * 2请求的参数{需要检验的数据}/{校验数据的类型}
     * 3返回的结果sysresult 包含ture和false
     */
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param , @PathVariable Integer type, String callback){
    Boolean flag=userService.checkUser(param,type);
    //int a=1/0;
    return  new JSONPObject(callback,SysResult.success(flag));

    }

    /**
     * 完成数据的回显
     *url:http://sso/jt.com/user/query/{ticket}
     * 参数ttcket数据
     * 返回时 Sysresult对象jsonp
     * */
    @RequestMapping("/query/{ticket}")
    public  JSONPObject findUserByTicket(@PathVariable String ticket,String callback){
        String userJSON = jedisCluster.get(ticket);
        if (StringUtils.isEmpty(userJSON)){
            return new JSONPObject(callback, SysResult.fail());
        }
        return  new JSONPObject(callback, SysResult.success(userJSON));

    }











}
