package com.iamxgw;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class, args);
    }

    @Bean(name = "shardedJedisPool")
    public ShardedJedisPool shardedJedisPool() {
        JedisShardInfo jedisShardInfo = new JedisShardInfo("47.100.178.16", 6379);
        jedisShardInfo.setPassword("gd-*GFfd87d***fd7#SSfd");
        List<JedisShardInfo> shardInfoList = Arrays.asList(jedisShardInfo);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, shardInfoList);
        return shardedJedisPool;
    }
}
