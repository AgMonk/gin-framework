package com.gin.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gin.database.config.cache.RedisMybatisCache;
import com.gin.security.entity.SystemUserAvatar;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 16:54
 */
@Mapper
@CacheNamespace(flushInterval = 5L * 60 * 1000, implementation = RedisMybatisCache.class)
public interface SystemUserAvatarDao extends BaseMapper<SystemUserAvatar> {
}
