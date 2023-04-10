package com.gin.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.security.dao.RelationUserRoleDao;
import com.gin.security.entity.RelationUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gin.security.service.RelationUserRoleService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RelationUserRoleServiceImpl extends ServiceImpl<RelationUserRoleDao, RelationUserRole> implements RelationUserRoleService {
}