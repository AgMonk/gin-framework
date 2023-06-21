package com.gin.security.controller;


import com.gin.security.Constant.Security;
import com.gin.spring.annotation.MyRestController;
import com.gin.security.bo.SystemUserBo;
import com.gin.common.constant.ApiPath;
import com.gin.security.dto.form.RelationUserRoleForm;
import com.gin.security.dto.form.UserRoleDelForm;
import com.gin.security.dto.form.UserRoleForm;
import com.gin.security.entity.RelationUserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.gin.security.service.RelationUserRoleService;
import com.gin.security.service.RolePermissionService;
import com.gin.security.service.SystemRoleService;
import com.gin.security.service.impl.SystemUserServiceImpl;
import com.gin.database.validation.EntityId;
import com.gin.spring.vo.response.Res;

import java.util.Collections;
import java.util.List;

import static com.gin.security.Constant.Security.NOT_CONFIG_ADMIN;


/**
 * 用户角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/21 10:06
 */

@MyRestController(SystemUserRoleController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemUserRoleController.GROUP_NAME)
@Slf4j
public class SystemUserRoleController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = SystemUserController.API_PREFIX + "/role";
    public static final String GROUP_NAME = "用户角色接口";


    private final RelationUserRoleService relationUserRoleService;
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;


    @PostMapping(ApiPath.ADD)
    @Operation(summary = "为指定用户添加角色", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleAdd(@RequestBody @Validated UserRoleForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(form.getUserId());
        systemRoleService.validateRoleId(form.getRoles().stream().map(RelationUserRoleForm::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.add(form.getUserId(), form.getRoles());
        return Res.of(roleList);
    }

    @PostMapping(ApiPath.CONFIG)
    @Operation(summary = "为指定用户配置角色", description = "如果给出的角色尚未持有,添加持有;如果已持有,更新过期时间;删除未给出的角色持有<br/>" + NOT_CONFIG_ADMIN)
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleConfig(@RequestBody @Validated UserRoleForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final Long userId = form.getUserId();
        final List<RelationUserRoleForm> roles = form.getRoles();
        rolePermissionService.forbiddenConfigAdminUser(userId);
        systemRoleService.validateRoleId(roles.stream().map(RelationUserRoleForm::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.config(userId, roles);
        return Res.of(roleList);
    }

    @PostMapping(ApiPath.DEL)
    @Operation(summary = "为指定用户删除角色", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Void> roleDel(@RequestBody @Validated UserRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(form.getUserId());
        relationUserRoleService.del(form.getUserId(), form.getRoleId());
        return Res.of(null);
    }

    @GetMapping(ApiPath.LIST)
    @Operation(summary = "查询指定用户的认证/授权信息")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserBo> roleList(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(rolePermissionService.listAuthorityByUserId(Collections.singleton(userId)).get(0));
    }
}