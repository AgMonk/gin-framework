package com.gin.security.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.security.dao.RelationRolePermissionDao;
import com.gin.security.entity.RelationRolePermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gin.security.service.RelationRolePermissionService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RelationRolePermissionServiceImpl extends ServiceImpl<RelationRolePermissionDao, RelationRolePermission> implements RelationRolePermissionService {
}