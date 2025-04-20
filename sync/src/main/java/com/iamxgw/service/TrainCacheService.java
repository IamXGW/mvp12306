package com.iamxgw.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @author IamXGW
 * @since 2025-04-12
 */
@Service
@Slf4j
public class TrainCacheService {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    private ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    private void safeClose(ShardedJedis shardedJedis) {
        if (shardedJedis == null) {
            return;
        }
        try {
            shardedJedis.close();
        } catch (Exception e) {
            log.error("jedis close exception", e);
        }
    }

    public void set(String key, String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = instance();
            shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error("jedis set exception, key:{}, value:{}", key, value, e);
        } finally {
            safeClose(shardedJedis);
        }
    }

    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = instance();
            return shardedJedis.get(key);
        } catch (Exception e) {
            log.error("jedis get exception, key:{}, value:{}", key, e);
            throw e;
        } finally {
            safeClose(shardedJedis);
        }
    }

    public void hset(String key, String field, String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = instance();
            shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            log.error("jedis hset exception, key:{}, field:{} value:{}", key, field, value, e);
            throw e;
        } finally {
            safeClose(shardedJedis);
        }
    }

    public String hget(String key, String field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = instance();
            return shardedJedis.hget(key, field);
        } catch (Exception e) {
            log.error("jedis hget exception, key:{}, field:{}", key, field, e);
            throw e;
        } finally {
            safeClose(shardedJedis);
        }
    }

    public void hincrBy(String key, String field, long value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = instance();
            shardedJedis.hincrBy(key, field, value);
        } catch (Exception e) {
            log.error("jedis hincrBy exception, key:{}, field:{} value:{}", key, field, value, e);
            throw e;
        } finally {
            safeClose(shardedJedis);
        }
    }
}