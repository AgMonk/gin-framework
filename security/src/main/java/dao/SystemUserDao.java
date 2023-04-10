package dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import config.redis.RedisMybatisCache;
import entity.SystemUser;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:44
 */
@Mapper
@CacheNamespace(flushInterval = 60 * 1000, implementation = RedisMybatisCache.class)
public interface SystemUserDao extends BaseMapper<SystemUser> {
}
