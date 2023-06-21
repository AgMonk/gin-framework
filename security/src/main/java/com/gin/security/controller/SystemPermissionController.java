package com.gin.security.controller;


import com.gin.security.Constant.Security;
import com.gin.spring.annotation.MyRestController;
import com.gin.common.constant.ApiPath;
import com.gin.security.dto.param.SystemPermissionPageParam;
import com.gin.security.entity.SystemPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import com.gin.route.annotation.MenuEntry;
import com.gin.route.annotation.MenuItem;
import com.gin.route.annotation.MenuPath;
import com.gin.security.service.SystemPermissionService;
import com.gin.security.vo.PermissionGroup;
import com.gin.spring.vo.response.Res;
import com.gin.database.vo.response.ResPage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 14:02
 */

@MyRestController(SystemPermissionController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = SystemPermissionController.GROUP_NAME)
@Slf4j
@MenuItem(title = "权限列表", description = "查询现有的粗粒度权限", order = 8, path = @MenuPath(title = "用户和权限", order = 1))
public class SystemPermissionController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/permission";
    public static final String GROUP_NAME = "权限接口";
    private final SystemPermissionService systemPermissionService;

    @GetMapping(ApiPath.GROUP)
    @Operation(summary = "分组查询权限", description = "用于角色权限的'配置'接口")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<PermissionGroup>> group(@SuppressWarnings("unused") HttpServletRequest request) {
        final Map<String, List<SystemPermission>> map = systemPermissionService.list().stream().collect(Collectors.groupingBy(SystemPermission::getGroupName));
        final List<PermissionGroup> data = map.keySet().stream()
                .map(name -> new PermissionGroup(name, map.get(name)))
                .sorted(Comparator.comparing(PermissionGroup::getGroupName))
                .toList();
        return Res.of(data);
    }

    @GetMapping(ApiPath.PAGE)
    @Operation(summary = "分页查询权限", description = "用于角色权限的'添加'接口")
    @PreAuthorize(Security.PRE_AUTHORITY_URI_OR_ADMIN)
    @MenuEntry
    public Res<ResPage<SystemPermission>> page(
            @ParameterObject SystemPermissionPageParam param,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(systemPermissionService.pageByParam(param, i -> i));
    }

}