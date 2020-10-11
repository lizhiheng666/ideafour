package com.jt.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class TestCluster {
    @Test
    public void test01(){
        //利用集合封装redis信息
        Set<HostAndPort> nodes=new HashSet<>();
        nodes.add(new HostAndPort("192.168.126.129",7000));
        nodes.add(new HostAndPort("192.168.126.129",7001));
        nodes.add(new HostAndPort("192.168.126.129",7002));
        nodes.add(new HostAndPort("192.168.126.129",7003));
        nodes.add(new HostAndPort("192.168.126.129",7004));
        nodes.add(new HostAndPort("192.168.126.129",7005));
        JedisCluster jedisCluster=new JedisCluster(nodes);
        jedisCluster.set("李志恒", "今天是元气满满的一天");
        System.out.println(jedisCluster.get("李志恒"));
    }
}
