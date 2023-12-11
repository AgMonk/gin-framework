package com.gin.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gin.database.config.cache.RedisMybatisCache;
import com.gin.security.entity.SystemUserPhone;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 15:44
 **/
@Mapper
@CacheNamespace(flushInterval = 5L * 60 * 1000, implementation = RedisMybatisCache.class)
public interface SystemUserPhoneDao extends BaseMapper<SystemUserPhone> {
}