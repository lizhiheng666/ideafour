package com.jt.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class TestSentinel {//完成哨兵的测试
    @Test
    public void test01(){
        /**
         * mastername 主机名称
         * sentinels 哨兵
         */
        Set<String> sentinel =new HashSet<>();
        String node ="192.168.126.129:26379";
        sentinel.add(node);
        JedisSentinelPool sentinelPool=new JedisSentinelPool("mymaster",sentinel);
        Jedis jedis = sentinelPool.getResource();//获取数据源
        jedis.set("sentinel","redis的哨兵已经配置成功");
        System.out.println(jedis.get("sentinel"));
    }
}
