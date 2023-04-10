package com.gin.database.config.redis.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import com.gin.common.utils.SpringContextUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Redis缓存
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/22 09:11
 */
@Slf4j
@RequiredArgsConstructor
public class CustomRedisCache implements Cache {
    /**
     * 缓存名称
     */
    @NotNull
    private final String id;
    /**
     * redis key 前缀
     */
    @NotNull
    private final String prefix;
    /**
     * 过期时间（秒）
     */
    private final long timeoutSeconds;

    /**
     * 清空缓存
     */
    @Override
    public final void clear() {
        log.debug("[Redis][{}] 清空", id);
        final Set<String> keys = getRedisTemplate().keys(getKey("*"));
        if (keys != null) {
            getRedisTemplate().delete(keys);
        }
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Object getObject(Object key) {
        final Object value = getRedisTemplate().opsForValue().get(getKey(key));
        log.debug("[Redis][{}] {}命中: {}", id, value == null ? "未" : "", key);
        return value;
    }

    @Override
    public final int getSize() {
        final Set<String> keys = getRedisTemplate().keys(getKey("*"));
        return keys != null ? keys.size() : 0;
    }

    @Override
    public final void putObject(Object key, Object value) {
        if (timeoutSeconds > 0) {
            log.debug("[Redis][{}] 保存: {} 过期时间 {} 秒", id, getLogKey(key), timeoutSeconds);
            getRedisTemplate().opsForValue().set(getKey(key), value, timeoutSeconds, TimeUnit.SECONDS);
        } else {
            log.debug("[Redis][{}] 保存: {}", id, getLogKey(key));
            getRedisTemplate().opsForValue().set(getKey(key), value);
        }
    }

    @Override
    public final Object removeObject(Object key) {
        log.debug("[Redis][{}] 移除: {}", id, getLogKey(key));
        return getRedisTemplate().opsForValue().getAndDelete(getKey(key));
    }

    /**
     * 在打印日志时使用的key
     * @param key key
     * @return 日志key
     */
    public String getLogKey(Object key) {
        return getRedisKey(key);
    }

    /**
     * 将key转换为在Redis中保存的key
     * @param key 原key
     * @return RedisKey
     */
    public String getRedisKey(Object key) {
        final String s = String.valueOf(key).replace("\n", "");
        return s.length() < 50 ? s : DigestUtils.md5DigestAsHex(s.getBytes());
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        //noinspection unchecked
        return (RedisTemplate<String, Object>) SpringContextUtils.getContext().getBean("jsonTemplate",
                                                                                       new TypeReference<RedisTemplate<String, Object>>() {
                                                                                       });
    }

    private String getKey(Object key) {
        return String.format("%s:%s", prefix, getRedisKey(key));
    }
}
