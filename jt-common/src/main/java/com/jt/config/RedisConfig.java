package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration  //标识我是配置类
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.nodes}")
    private String nodes;
    /**
     * spring整合redis集群
     */
    @Bean
    public JedisCluster jedisCluster(){
        String[] strNodes =nodes.split(",");
        Set<HostAndPort> set =new HashSet<>();
        for (String node : strNodes) {
            String host =node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            set.add(new HostAndPort(host, port));
        }
                
        return new JedisCluster(set);
    }
   /* /**
     * spring整合redis分片机制
     *//*
    @Bean
    public ShardedJedis shardedJedis(){
        String[] strNodes=nodes.split(",");
        List<JedisShardInfo> shards =new ArrayList<>();
        for (String node : strNodes) {
            String host=node.split(":")[0];
            int port=Integer.parseInt(node.split(":")[1]);

            JedisShardInfo info=new JedisShardInfo(host,port);
            shards.add(info);
        }
        ShardedJedis shardedJedis=new ShardedJedis(shards);
        return shardedJedis;
    }*/

 /*   @Value("${redis.host}")
    private String  host;
    @Value("${redis.port}")
    private Integer port;

    @Bean
    public Jedis jedis(){

        return new Jedis(host,port);
    }*/
}
