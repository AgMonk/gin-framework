package controller;


import Constant.Security;
import annotation.MyRestController;
import constant.ApiPath;
import dto.form.SystemRoleDelForm;
import dto.form.SystemRoleForm;
import dto.param.SystemRolePageParam;
import entity.SystemRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import route.annotation.MenuEntry;
import route.annotation.MenuItem;
import route.annotation.MenuPath;
import service.RolePermissionService;
import service.SystemRoleService;
import service.impl.SystemRoleServiceImpl;
import validation.EntityId;
import vo.SystemRoleVo;
import vo.response.Res;
import vo.response.ResPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 14:11
 */
@MyRestController(SystemRoleController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemRoleController.GROUP_NAME)
@Slf4j
@MenuItem(title = "角色管理", description = "角色的增删改查, 管理角色持有的权限", order = 9, path = @MenuPath(title = "用户和权限", order = 1))
public class SystemRoleController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/role";
    public static final String GROUP_NAME = "角色管理接口";
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;


    @PostMapping(ApiPath.ADD)
    @Operation(summary = "添加角色", description = "返回添加完成的角色")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleAdd(
            @RequestBody @Validated @NotEmpty @Parameter(description = "角色列表") List<SystemRoleForm> roles,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<SystemRoleVo> data = systemRoleService.addByParam(roles).stream().map(SystemRoleVo::new).collect(Collectors.toList());
        return Res.of(data);
    }

    @PostMapping(ApiPath.DEL)
    @Operation(summary = "删除角色", description = "注意:将连带删除所有对该角色的持有")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleDel(@RequestBody @Validated SystemRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final List<Long> roleId = form.getRoleId();
        systemRoleService.validateRoleId(roleId);
        final List<SystemRoleVo> res = rolePermissionService.roleDel(roleId).stream().map(SystemRoleVo::new).toList();
        return Res.of(res, "删除成功");
    }

    @GetMapping(ApiPath.PAGE)
    @Operation(summary = "分页查询角色")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @MenuEntry
    public ResPage<SystemRoleVo> rolePage(
            @ParameterObject SystemRolePageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemRoleService.pageByParam(pageParam, SystemRoleVo::new);
    }

    @PostMapping(ApiPath.UPDATE)
    @Operation(summary = "修改角色", description = "返回修改完成的角色")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRoleVo> roleUpdate(
            @RequestBody @Validated SystemRoleForm param,
            @RequestParam @EntityId(service = SystemRoleServiceImpl.class) @Parameter(description = "角色id") Long roleId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final SystemRole systemRole = systemRoleService.updateByIdParam(roleId, param);
        final SystemRoleVo vo = new SystemRoleVo(systemRole);
        return Res.of(vo);
    }
}
