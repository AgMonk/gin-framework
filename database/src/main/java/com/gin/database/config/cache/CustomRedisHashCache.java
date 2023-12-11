package com.gin.database.config.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gin.spring.utils.SpringContextUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

/**
 * 自定义Redis Hash缓存
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/22 09:11
 */
@Slf4j
@RequiredArgsConstructor
public class CustomRedisHashCache implements Cache {
    /**
     * 缓存名称
     */
    @NotNull
    private final String id;
    /**
     * redis key
     */
    @NotNull
    private final String key;
    /**
     * 过期时间（秒）
     */
    private final long timeoutSeconds;

    /**
     * 清空缓存
     */
    @Override
    public final void clear() {
        log.info("[Redis][{}] 清空缓存", key);
        getRedisTemplate().delete(key);
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Object getObject(Object key) {
        final Object value = getOps().get(getHashKey(key));
        log.debug("[Redis][{}] {}命中: {}", id, value == null ? "未" : "", getLogKey(key));
        return value;
    }

    @Override
    public final int getSize() {
        final Long size = getOps().size();
        return size == null ? 0 : Math.toIntExact(size);
    }

    @Override
    public final void putObject(Object key, Object value) {
        if (timeoutSeconds > 0) {
            log.debug("[Redis][{}] 保存: {} 过期时间 {} 秒", id, getLogKey(key), timeoutSeconds);
            getOps().put(getHashKey(key), value);
            if (getSize() == 1) {
                getOps().expire(timeoutSeconds, TimeUnit.SECONDS);
            }
        } else {
            log.debug("[Redis][{}] 保存: {}", id, getLogKey(key));
            getOps().put(getHashKey(key), value);
        }
    }

    @Override
    public final Object removeObject(Object key) {
        log.info("[Redis][{}] 移除: {}", id, getLogKey(key));
        final Object res = getObject(key);
        getOps().delete(key);
        return res;
    }

    /**
     * 将key转换为在Hash中保存的key
     *
     * @param key 原key
     * @return hashKey
     */
    public String getHashKey(Object key) {
        final String s = String.valueOf(key).replace("\n", "")
                .replace("\r", "")
                .replace("  ", " ");
        return s.length() < 50 ? s : DigestUtils.md5DigestAsHex(s.getBytes());
    }

    /**
     * 在打印日志时使用的key
     *
     * @param key key
     * @return 日志key
     */
    public String getLogKey(Object key) {
        return getHashKey(key);
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        //noinspection unchecked
        return (RedisTemplate<String, Object>) SpringContextUtils.getContext().getBean("jsonTemplate",
                new TypeReference<RedisTemplate<String, Object>>() {
                });
    }

    private BoundHashOperations<String, String, Object> getOps() {
        return getRedisTemplate().boundHashOps(key);
    }
}
