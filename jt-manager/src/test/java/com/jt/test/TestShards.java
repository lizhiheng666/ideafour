package com.jt.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class TestShards {
    /**
     * 有三台redis 测试一下
     * 实现数据的存储
     */
    @Test
    public void test01(){
        List<JedisShardInfo> shards =new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.126.129",6379));
        shards.add(new JedisShardInfo("192.168.126.129",6380));
        shards.add(new JedisShardInfo("192.168.126.129",6381));
        ShardedJedis shardedJedis=new ShardedJedis(shards);
        shardedJedis.set("你好啊","我是李老八");
        System.out.println(shardedJedis.get("你好啊"));
    }
}
