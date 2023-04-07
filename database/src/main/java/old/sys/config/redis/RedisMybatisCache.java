package old.sys.config.redis;

import com.gin.springboot3template.sys.utils.StrUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RedisRedis-Mybatis缓存
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/4/18 10:40
 **/
@Slf4j
public class RedisMybatisCache extends CustomRedisCache {
    public RedisMybatisCache(String id) {
        super(StrUtils.getSimplePackageName(id), "MyBatis:" + StrUtils.getSimplePackageName(id), 300);
        log.info("初始化缓存: " + id);
    }

    /**
     * 在打印日志时使用的key
     * @param key key
     * @return 日志key
     */
    @Override
    public String getLogKey(Object key) {
        final String[] split = String.valueOf(key).split(":");
        if (split.length < 6) {
            return super.getLogKey(key);
        }
        return split[5].replace("\n", "").replace("  ", "");
    }
}
