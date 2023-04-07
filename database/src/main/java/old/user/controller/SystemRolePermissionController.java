package old.user.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.user.dto.form.SystemRolePermissionForm;
import com.gin.springboot3template.user.entity.SystemPermission;
import com.gin.springboot3template.user.service.RelationRolePermissionService;
import com.gin.springboot3template.user.service.RolePermissionService;
import com.gin.springboot3template.user.service.SystemPermissionService;
import com.gin.springboot3template.user.service.SystemRoleService;
import com.gin.springboot3template.user.service.impl.SystemRoleServiceImpl;
import com.gin.springboot3template.user.vo.SystemRolePermissionVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 角色权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/21 10:01
 */

@MyRestController(SystemRolePermissionController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemRolePermissionController.GROUP_NAME)
@Slf4j
public class SystemRolePermissionController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = SystemRoleController.API_PREFIX + "/permission";
    public static final String GROUP_NAME = "角色权限接口";

    private final RelationRolePermissionService relationRolePermissionService;
    private final SystemPermissionService systemPermissionService;
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;


    @PostMapping(Constant.Api.ADD)
    @Operation(summary = "为角色添加权限", description = "返回添加的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionAdd(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        systemRoleService.forbiddenConfigAdminRole(Collections.singleton(form.getRoleId()));

        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //添加权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.add(form.getRoleId(), permIds)));
    }

    @PostMapping(Constant.Api.CONFIG)
    @Operation(summary = "为角色配置权限", description = "如果给出的权限尚未持有,添加持有;删除未给出的权限持有<br/>返回配置的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionConfig(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        systemRoleService.forbiddenConfigAdminRole(Collections.singleton(form.getRoleId()));

        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //移除权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.config(form.getRoleId(), permIds)));
    }

    @PostMapping(Constant.Api.DEL)
    @Operation(summary = "为角色移除权限", description = "返回移除的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionDel(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        systemRoleService.forbiddenConfigAdminRole(Collections.singleton(form.getRoleId()));
        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //移除权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.del(form.getRoleId(), permIds)));
    }

    @GetMapping(Constant.Api.LIST)
    @Operation(summary = "查询角色持有的权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemPermission>> permissionList(
            @RequestParam @EntityId(service = SystemRoleServiceImpl.class) @Parameter(description = "角色id") Long roleId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final HashMap<Long, List<SystemPermission>> map = rolePermissionService.findRolePermissionMap(Collections.singleton(roleId));
        return Res.of(map.get(roleId));
    }
}