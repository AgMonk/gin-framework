package com.gin.security.controller;


import com.gin.operationlog.annotation.OpLog;
import com.gin.operationlog.controller.OperationLogController;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.subclass.PasswordSubClass;
import com.gin.route.annotation.MenuItem;
import com.gin.security.Constant.Security;
import com.gin.security.dto.form.LoginForm;
import com.gin.security.dto.form.RegForm;
import com.gin.security.dto.form.SystemUserInfoForm;
import com.gin.security.entity.SystemUser;
import com.gin.security.entity.SystemUserAvatar;
import com.gin.security.entity.SystemUserInfo;
import com.gin.security.properties.UserProperties;
import com.gin.security.service.RolePermissionService;
import com.gin.security.service.SystemUserAvatarService;
import com.gin.security.service.SystemUserInfoService;
import com.gin.security.service.SystemUserService;
import com.gin.security.utils.MySecurityUtils;
import com.gin.security.validation.Password;
import com.gin.security.vo.MyUserDetailsVo;
import com.gin.security.vo.SystemUserInfoVo;
import com.gin.security.vo.SystemUserVo;
import com.gin.spring.annotation.MyRestController;
import com.gin.spring.exception.BusinessException;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.gin.security.Constant.Security.PASSWORD_MAX_LENGTH;
import static com.gin.security.Constant.Security.PASSWORD_MIN_LENGTH;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 用户接口
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController(SystemUserController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "用户接口")
@Slf4j
@MenuItem(title = "用户中心", description = "当前用户对自己的信息查询和操作")
public class SystemUserController implements OperationLogController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/user";
    private final SystemUserService systemUserService;
    private final SystemUserInfoService systemUserInfoService;
    private final UserProperties userProperties;
    private final RolePermissionService rolePermissionService;
    private final SystemUserAvatarService systemUserAvatarService;

    @PostMapping("changePwd")
    @Operation(summary = "修改密码", description = "修改成功后会自动登出,需要重新登陆")
    @OpLog(type = OperationType.UPDATE, mainClass = SystemUser.class, mainId = "#currentUserId", subClass = PasswordSubClass.class, requestParam = false, responseResult = false)
    public void changePwd(
            @SuppressWarnings("unused") HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam @Parameter(description = "旧密码") String oldPass,
            @RequestParam @Parameter(description = "新密码,长度范围为 [" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "]") @Password String newPass
    ) throws ServletException, IOException {
        final Long userId = getUserId();
        systemUserService.changePwd(userId, oldPass, newPass);
//        登出
        request.getRequestDispatcher(Security.LOGOUT_URI).forward(request, response);
    }

    @PostMapping("login")
    @Operation(summary = "登陆", description = "假登陆接口 ,用于生成 doc;<br/>需要先获取验证码,参数可以传body也可以传form")
    public Res<MyUserDetailsVo> login(@RequestBody @Validated @SuppressWarnings("unused") LoginForm loginForm) {
        System.out.println("login...");
        return null;
    }

    @PostMapping("logout")
    @Operation(summary = "登出", description = "假登出接口 ,用于生成 doc")
    public void logout() {
        System.out.println("logout...");
    }

    /**
     * 主实体类型
     *
     * @return 主实体类型
     */
    @Override
    public Class<?> mainClass() {
        return SystemUser.class;
    }

    /**
     * 主实体ID
     *
     * @return 主实体ID
     */
    @Override
    public Long mainId(Long mainId) {
        return getUserId();
    }

    @PostMapping("reg")
    @Operation(summary = "注册用户")
    public Res<SystemUserVo> reg(@RequestBody @Validated RegForm regForm) {
        if (!userProperties.isRegEnable()) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, "注册功能已关闭");
        }
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "注册成功");
    }

    @PostMapping("token")
    @Operation(summary = "查询自己的认证/授权信息", description = "包含用户名,ID,账号状态,权限信息;<br/>可以用来查询登陆状态,以及更新CSRF TOKEN")
    public Res<MyUserDetailsVo> token() {
        return Res.of(MyUserDetailsVo.of());
    }

    @PostMapping(value = "avatar/delete")
    @Operation(summary = "用户头像删除")
    public Res<SystemUserAvatar> userAvatarDelete() {
        return Res.of(systemUserAvatarService.deleteByUserId(getUserId()), "删除成功");
    }

    @PostMapping(value = "avatar/upload", consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "用户头像上传")
    public Res<SystemUserAvatar> userAvatarUpload(MultipartFile file) throws IOException {
        final Long userId = getUserId();

        systemUserAvatarService.validateMultipartFile(file);

        systemUserAvatarService.deleteByUserId(userId);
        return Res.of(systemUserAvatarService.uploadWithUserId(file, userId), "上传成功");
    }

    @GetMapping("userInfoFind")
    @Operation(summary = "查询自己的个人信息")
    public Res<SystemUserInfoVo> userInfoFind() {
        final Long userId = getUserId();
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfoVo vo = new SystemUserInfoVo(userInfo);

        //用户头像查询
        final SystemUserAvatar avatar = systemUserAvatarService.getByUserId(userId);
        if (avatar != null) {
            vo.setAvatar(avatar.getFilePath());
        }

        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "修改自己的个人信息")
    @OpLog(type = OperationType.UPDATE, mainClass = SystemUser.class, mainId = "#currentUserId", subClass = SystemUserInfo.class
            , preExp = {"@systemUserInfoServiceImpl.getByUserId(#currentUserId)", "#form.build(#currentUserId)"}
    )
    public Res<SystemUserInfoVo> userInfoUpdate(@RequestBody @Validated SystemUserInfoForm form) {
        final SystemUserInfo info = systemUserInfoService.saveOrUpdate(getUserId(), form);
        return Res.of(new SystemUserInfoVo(info), "修改成功");
    }

    private static Long getUserId() {
        return MySecurityUtils.currentUserDetails().getId();
    }


}
