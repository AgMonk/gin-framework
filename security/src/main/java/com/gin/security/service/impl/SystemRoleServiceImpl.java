package com.gin.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.security.dao.SystemRoleDao;
import com.gin.security.entity.SystemRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gin.security.service.SystemRoleService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleDao, SystemRole> implements SystemRoleService {
}