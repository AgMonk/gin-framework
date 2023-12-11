package com.gin.operationlog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gin.database.config.cache.RedisMybatisCache;
import com.gin.operationlog.entity.SystemOperationLogOld;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:11
 */
@Mapper
@CacheNamespace(flushInterval = 5L * 60 * 1000, implementation = RedisMybatisCache.class)
public interface SystemOperationLogOldDao extends BaseMapper<SystemOperationLogOld> {
}
