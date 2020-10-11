package com.jt.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.unit.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;


@Service//使用dubbo的注解
public class DobbuUserServiceImpl implements DubboUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 密码需要加密
     * 邮箱使用手机代替
     * @param user
     */
    @Override
    public void saveUser(User user) {
        String md5Password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password).setEmail(user.getPhone());
        userMapper.insert(user);
    }

    /**
     * 目的 验证用户额信息是否有效 并且实现单点登陆
     * 步骤
     *      1：校验用户名和密码是否正确（明文转化问密文）
     *      2：查询数据库检查是否有结果
     *      3：如果有则动态生成ticket信息（UUID），将对象转化为json
     *      4：将数据保存在redis中 并且保存超时时间
     *      5：返回当前用户登陆的密钥。
     * @param user
     * @return
     */
    @Override
    public String doLogin(User user) {
        String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password",md5Pass);
        //获取的是所有的东西 包含涉密的信息
        User userDB=userMapper.selectOne(queryWrapper);
        if (userDB==null){
            return  null;
        }
        //程序执行到此说明账号密码正确
        String ticket= UUID.randomUUID().toString().replace("-", "");
        //脱敏处理余额/密码/手机哈/家庭住址
        userDB.setPassword("woshinidie");//只更换数据 数据库不影响
        String userjSON= ObjectMapperUtil.toJSON(userDB);
        jedisCluster.setex(ticket, 7*24*60*60, userjSON);

        return ticket;
    }
}
