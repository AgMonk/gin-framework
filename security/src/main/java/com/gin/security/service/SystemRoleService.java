package com.gin.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.database.service.MyService;
import com.gin.security.Constant.Security;
import com.gin.security.Constant.User;
import com.gin.security.dto.form.SystemRoleForm;
import com.gin.security.entity.SystemRole;
import com.gin.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:57
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemRoleService extends MyService<SystemRole> {
    org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SystemRoleService.class);

    /**
     * 添加角色
     * @param param 参数
     * @return 添加好的角色
     */
    default List<SystemRole> addByParam(Collection<SystemRoleForm> param) {
        final List<SystemRole> roles = param.stream().map(SystemRoleForm::build).toList();
        saveBatch(roles);
        return roles;
    }

    /**
     * 不能分配/取消分配 admin 角色
     * @param roleId 角色id
     */
    default void forbiddenConfigAdminRole(Collection<Long> roleId) {
        final SystemRole systemRole = getByName(User.ADMIN);
        if (roleId.contains(systemRole.getId())) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, Security.FORBIDDEN_CONFIG_ADMIN);
        }
    }

    /**
     * 通过名称查询角色
     * @param name 名称
     * @return 角色
     */
    default SystemRole getByName(String name) {
        final QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.eq("name", name);
        return getOne(qw);
    }

    /**
     * 根据角色名查询一个角色,如果不存在则按照给出的参数创建,并返回
     * @param systemRole 当不存在时的创建参数
     * @return 角色
     */
    default SystemRole getOrCreateByName(SystemRole systemRole) {
        final SystemRole role = getByName(systemRole.getName());
        if (role == null) {
            LOG.info("角色 {} 不存在,执行创建", systemRole.getName());
            save(systemRole);
            return systemRole;
        }
        return role;
    }

    /**
     * 通过名称查询角色
     * @param name 名称
     * @return 角色
     */
    default List<SystemRole> listByName(Collection<String> name) {
        final QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.in("name", name);
        return list(qw);
    }

    /**
     * 修改角色
     * @param roleId 角色id
     * @param param  参数
     * @return 修改后的角色
     */
    default SystemRole updateByIdParam(long roleId, SystemRoleForm param) {
        final SystemRole entity = param.build();
        entity.setId(roleId);
        entity.setTimeUpdate(System.currentTimeMillis() / 1000);
        updateById(entity);
        return entity;
    }

    /**
     * 校验角色ID存在
     * @param roleId 角色ID
     */
    default void validateRoleId(Collection<Long> roleId) {
        if (CollectionUtils.isEmpty(roleId)) {
            return;
        }
        final List<Long> idNotExists = findNotExistsId(roleId);
        if (idNotExists.size() > 0) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST,
                                       "参数校验异常,如下角色ID不存在",
                                       idNotExists.stream().map(String::valueOf).collect(Collectors.toList()));
        }
    }
}