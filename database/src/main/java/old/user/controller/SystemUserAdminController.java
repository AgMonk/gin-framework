package old.user.controller;

import com.gin.springboot3template.route.annotation.MenuEntry;
import com.gin.springboot3template.route.annotation.MenuItem;
import com.gin.springboot3template.route.annotation.MenuPath;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.sys.vo.response.ResPage;
import com.gin.springboot3template.user.dto.form.RegForm;
import com.gin.springboot3template.user.dto.form.ResetPasswordForm;
import com.gin.springboot3template.user.dto.form.SystemUserInfoForm;
import com.gin.springboot3template.user.dto.param.SystemUserPageParam;
import com.gin.springboot3template.user.entity.SystemUser;
import com.gin.springboot3template.user.entity.SystemUserInfo;
import com.gin.springboot3template.user.service.RolePermissionService;
import com.gin.springboot3template.user.service.SystemUserInfoService;
import com.gin.springboot3template.user.service.SystemUserService;
import com.gin.springboot3template.user.service.impl.SystemUserServiceImpl;
import com.gin.springboot3template.user.vo.SystemUserInfoVo;
import com.gin.springboot3template.user.vo.SystemUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.UUID;

import static com.gin.springboot3template.sys.bo.Constant.Messages.NOT_CONFIG_ADMIN;

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
public class SystemUserAdminController {
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
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserVo> create(
            @RequestBody @Validated RegForm regForm,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "创建成功");
    }

    @PostMapping("lock")
    @Operation(summary = "锁定/解锁指定用户", description = "切换锁定和解锁状态;<br/>锁定用户不能登陆;<br/>" + NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
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

    @GetMapping(Constant.Api.PAGE)
    @Operation(summary = "分页查询用户账号信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @MenuEntry
    public ResPage<SystemUserVo> page(
            @ParameterObject SystemUserPageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemUserService.pageByParam(pageParam, SystemUserVo::new);
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户的密码", description = NOT_CONFIG_ADMIN + "<br/>返回新密码")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
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
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
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
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfoVo> userInfoUpdate(
            @SuppressWarnings("unused") HttpServletRequest request,
            @RequestBody @Validated SystemUserInfoForm param,
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId
    ) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        final SystemUserInfo userInfo = systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(new SystemUserInfoVo(userInfo), "修改成功");
    }

}
