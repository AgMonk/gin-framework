package com.gin.security.controller;


import com.gin.operationlog.controller.OperationLogController;
import com.gin.operationlog.dto.param.OperationLogPageParam;
import com.gin.operationlog.vo.SubClassOption;
import com.gin.operationlog.vo.SystemOperationLogVo;
import com.gin.security.Constant.Security;
import com.gin.spring.annotation.MyRestController;
import com.gin.common.constant.ApiPath;
import com.gin.security.dto.form.RegForm;
import com.gin.security.dto.form.ResetPasswordForm;
import com.gin.security.dto.form.SystemUserInfoForm;
import com.gin.security.dto.param.SystemUserPageParam;
import com.gin.security.entity.SystemUser;
import com.gin.security.entity.SystemUserInfo;
import com.gin.spring.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.gin.route.annotation.MenuEntry;
import com.gin.route.annotation.MenuItem;
import com.gin.route.annotation.MenuPath;
import com.gin.security.service.RolePermissionService;
import com.gin.security.service.SystemUserInfoService;
import com.gin.security.service.SystemUserService;
import com.gin.security.service.impl.SystemUserServiceImpl;
import com.gin.database.validation.EntityId;
import com.gin.security.vo.SystemUserInfoVo;
import com.gin.security.vo.SystemUserVo;
import com.gin.spring.vo.response.Res;
import com.gin.database.vo.response.ResPage;

import java.util.List;
import java.util.UUID;

import static com.gin.security.Constant.Security.NOT_CONFIG_ADMIN;


/**
 * 用户接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController(SystemUserAdminController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemUserAdminController.GROUP_NAME)
@Slf4j
@MenuItem(title = "用户管理", description = "用户的管理, 管理用户持有的角色", order = 10, path = @MenuPath(title = "用户和权限", order = 1))
public class SystemUserAdminController  implements OperationLogController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = SystemUserController.API_PREFIX + "/admin";
    public static final String GROUP_NAME = "用户管理接口";
    private final SystemUserService systemUserService;
    private final SystemUserInfoService systemUserInfoService;
    private final RolePermissionService rolePermissionService;

    @PostMapping("create")
    @Operation(summary = "创建用户")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserVo> create(
            @RequestBody @Validated RegForm regForm,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "创建成功");
    }

    @PostMapping("lock")
    @Operation(summary = "锁定/解锁指定用户", description = "切换锁定和解锁状态;<br/>锁定用户不能登陆;<br/>" + NOT_CONFIG_ADMIN)
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Void> lock(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        final SystemUser user = systemUserService.getById(userId);
        final String message = user.getAccountNonLocked() ? "已锁定" : "已解锁";
        user.setAccountNonLocked(!user.getAccountNonLocked());
        systemUserService.updateById(user);
        return Res.of(null, message);
    }

    @GetMapping(ApiPath.PAGE)
    @Operation(summary = "分页查询用户账号信息")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @MenuEntry
    public ResPage<SystemUserVo> page(
            @ParameterObject SystemUserPageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemUserService.pageByParam(pageParam, SystemUserVo::new);
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户的密码", description = NOT_CONFIG_ADMIN + "<br/>返回新密码")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<String> reset(@RequestBody @Validated ResetPasswordForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final Long userId = form.getUserId();
        final String newPass = form.getNewPass();
        rolePermissionService.forbiddenConfigAdminUser(userId);
        String pwd = ObjectUtils.isEmpty(newPass) ? UUID.randomUUID().toString() : newPass;
        systemUserService.changePwd(userId, pwd);
        return Res.of(pwd, "修改成功");
    }


    @GetMapping("userInfoFind")
    @Operation(summary = "查询指定用户的个人信息")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfoVo> userInfoFind(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfoVo vo = new SystemUserInfoVo(userInfo);
        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "修改指定用户的个人信息", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfoVo> userInfoUpdate(
            @SuppressWarnings("unused") HttpServletRequest request,
            @RequestBody @Validated SystemUserInfoForm param,
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId
    ) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        final SystemUserInfo userInfo = systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(new SystemUserInfoVo(userInfo), "修改成功");
    }

    /**
     * 主实体类型
     *
     * @return 主实体类型
     */
    @Override
    public @NotNull Class<?> mainClass() {
        return null;
    }

    /**
     * 主实体ID
     *
     * @param mainId 用户传入的主实体Id
     * @return 主实体ID
     */
    @Nullable
    @Override
    public Long mainId(Long mainId) {
        return mainId;
    }

    /**
     * 列出该主实体类型(和主实体ID)下, 所有的副实体类型,及每个副实体类型下的操作类型
     *
     * @param old     是否查询旧日志
     * @param mainId  主实体Id ， 是否由用户指定由接口决定
     * @param request 请求
     * @return 所有的副实体类型, 及每个副实体类型下的操作类型
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SubClassOption>> getLogOptions(Boolean old, Long mainId, HttpServletRequest request) {
        return OperationLogController.super.getLogOptions(old, mainId, request);
    }

    /**
     * 日志分页查询
     *
     * @param old     是否查询旧日志
     * @param param   查询参数
     * @param request 请求
     * @return 日志
     */
    @Override
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemOperationLogVo> getLogPage(Boolean old, OperationLogPageParam param, HttpServletRequest request) {
        return OperationLogController.super.getLogPage(old, param, request);
    }
}
