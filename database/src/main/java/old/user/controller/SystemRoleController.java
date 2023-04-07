package old.user.controller;

import com.gin.springboot3template.route.annotation.MenuEntry;
import com.gin.springboot3template.route.annotation.MenuItem;
import com.gin.springboot3template.route.annotation.MenuPath;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.sys.vo.response.ResPage;
import com.gin.springboot3template.user.dto.form.SystemRoleDelForm;
import com.gin.springboot3template.user.dto.form.SystemRoleForm;
import com.gin.springboot3template.user.dto.param.SystemRolePageParam;
import com.gin.springboot3template.user.entity.SystemRole;
import com.gin.springboot3template.user.service.RolePermissionService;
import com.gin.springboot3template.user.service.SystemRoleService;
import com.gin.springboot3template.user.service.impl.SystemRoleServiceImpl;
import com.gin.springboot3template.user.vo.SystemRoleVo;
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


    @PostMapping(Constant.Api.ADD)
    @Operation(summary = "添加角色", description = "返回添加完成的角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleAdd(
            @RequestBody @Validated @NotEmpty @Parameter(description = "角色列表") List<SystemRoleForm> roles,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<SystemRoleVo> data = systemRoleService.addByParam(roles).stream().map(SystemRoleVo::new).collect(Collectors.toList());
        return Res.of(data);
    }

    @PostMapping(Constant.Api.DEL)
    @Operation(summary = "删除角色", description = "注意:将连带删除所有对该角色的持有")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleDel(@RequestBody @Validated SystemRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final List<Long> roleId = form.getRoleId();
        systemRoleService.validateRoleId(roleId);
        final List<SystemRoleVo> res = rolePermissionService.roleDel(roleId).stream().map(SystemRoleVo::new).toList();
        return Res.of(res, "删除成功");
    }

    @GetMapping(Constant.Api.PAGE)
    @Operation(summary = "分页查询角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @MenuEntry
    public ResPage<SystemRoleVo> rolePage(
            @ParameterObject SystemRolePageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemRoleService.pageByParam(pageParam, SystemRoleVo::new);
    }

    @PostMapping(Constant.Api.UPDATE)
    @Operation(summary = "修改角色", description = "返回修改完成的角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
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
