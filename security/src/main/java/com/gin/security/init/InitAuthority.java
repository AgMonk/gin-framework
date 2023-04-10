package com.gin.security.init;

import com.gin.security.Constant.Role;
import com.gin.security.Constant.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.security.controller.SystemPermissionController;
import com.gin.security.controller.SystemRoleController;
import com.gin.security.controller.SystemRolePermissionController;
import com.gin.security.controller.SystemUserRoleController;
import com.gin.security.dto.form.RegForm;
import com.gin.security.dto.form.RelationUserRoleForm;
import com.gin.security.entity.RelationRolePermission;
import com.gin.security.entity.SystemPermission;
import com.gin.security.entity.SystemRole;
import com.gin.security.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.gin.common.utils.SpringContextUtils;
import com.gin.common.utils.reflect.ReflectUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * 初始化 超管用户 角色 权限
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class InitAuthority implements ApplicationRunner {
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;
    private final RelationUserRoleService relationUserRoleService;
    private final SystemUserService systemUserService;
    private SystemRole adminRole;
    /**
     * 所有权限
     */
    private List<SystemPermission> fullPermission;
    private SystemRole roleManager;

    private static Class<?> getClass(Object object) throws ClassNotFoundException {
        final String name = object.getClass().getName();
        final String className = name.contains("$") ? name.substring(0, name.indexOf("$")) : name;
        return Class.forName(className);
    }

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("权限初始化");

        initPermissions();

        initRoles();

        initAdminUser();
    }

    /**
     * 初始化超管账号
     */
    private void initAdminUser() {
        //如果超管账号不存在 则注册
        final RegForm regForm = new RegForm();
        regForm.setUsername(User.ADMIN);
        regForm.setPassword(User.ADMIN_PASSWORD);
        regForm.setNickname(User.ADMIN_NICKNAME);
        rolePermissionService.initUser(regForm, Collections.singleton(new RelationUserRoleForm(this.adminRole.getId(), 0L)));
    }

    /**
     * 扫描接口,筛选出使用粗粒度校验的接口,将其路径作为权限字符串写入数据库,并记录一些其他信息
     */
    private void initPermissions() {
        // controller 接口
        final Collection<Object> controllers = SpringContextUtils.getContext().getBeansWithAnnotation(RequestMapping.class).values();

        // 扫描类得到的权限数据 新数据 无id
        List<SystemPermission> permissions = controllers.stream().flatMap(controller -> {
            final Class<?> controllerClass;
            try {
                controllerClass = getClass(controller);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return Stream.ofNullable(null);
            }
            //接口路径前缀
            final List<String> prePaths = ReflectUtils.getApiPath(controllerClass);
            final Tag tag = controllerClass.getAnnotation(Tag.class);

            return Arrays.stream(controllerClass.getDeclaredMethods()).flatMap(method -> {
                final List<String> apiPaths = ReflectUtils.getApiPath(method);
                final ArrayList<SystemPermission> list = new ArrayList<>();
                final Operation operation = method.getAnnotation(Operation.class);
                final PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                if (CollectionUtils.isEmpty(apiPaths) || preAuthorize == null) {
                    return null;
                }
                for (String prePath : prePaths) {
                    for (String apiPath : apiPaths) {
                        final String path = (prePath + "/" + apiPath).replaceAll("//", "/");
                        list.add(new SystemPermission(path, tag, operation, preAuthorize));
                    }
                }
                return list.stream();
            });
        }).toList();

        // 更新数据
        this.fullPermission = systemPermissionService.updateFromController(new ArrayList<>(permissions));

        if (!CollectionUtils.isEmpty(this.fullPermission)) {
            log.info("移除角色对已移除权限的持有");
            final QueryWrapper<RelationRolePermission> qw = new QueryWrapper<>();
            qw.notInSql("permission_id", String.format("select id from %s", SystemPermission.TABLE_NAME));
            relationRolePermissionService.remove(qw);
        }
    }

    /**
     * 初始化角色,创建 角色 超管(admin) 角色管理员(roleAdmin)
     */
    private void initRoles() {
        //检查 admin 角色是否存在 不存在则创建 存在则赋值
        final SystemRole adminRole = new SystemRole();
        adminRole.setName(Role.ADMIN);
        adminRole.setNameZh("超级管理员");
        adminRole.setRemark("预设超级管理员,不允许修改");
        adminRole.setDescription("预设超级管理员,不允许修改");
        this.adminRole = rolePermissionService.initRole(adminRole);

        //检查 角色管理员 角色是否存在 不存在则创建 存在则赋值
        final SystemRole roleManager = new SystemRole();
        roleManager.setName(Role.ROLE_MANAGER);
        roleManager.setNameZh("角色管理员");
        roleManager.setRemark("预设角色管理员,不允许修改其权限");
        roleManager.setDescription("预设角色管理员,不允许修改其权限");
        this.roleManager = rolePermissionService.initRole(roleManager,
                                                          List.of("/sys/user/admin/page"),
                                                          List.of(SystemRoleController.GROUP_NAME,
                                                                  SystemRolePermissionController.GROUP_NAME,
                                                                  SystemUserRoleController.GROUP_NAME,
                                                                  SystemPermissionController.GROUP_NAME));


    }
}
