package init;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import entity.SystemRole;
import service.RolePermissionService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Set;

/**
 * 初始化单个模块管理员 @Order 应当在10-50之间
 * @author bx002
 */
@Getter
@Slf4j
@RequiredArgsConstructor
public abstract class InitModuleManager implements ApplicationRunner {
    private final RolePermissionService rolePermissionService;

    /**
     * 任务内容
     */
    @Override
    public final void run(ApplicationArguments args) {
        final SystemRole role = systemRole();
        log.info("初始化模块管理员: " + role.getNameZh());
        rolePermissionService.initRole(role, path(), groupName());
    }

    /**
     * 接口分组名称
     * @return 接口分组名称
     */
    @Nullable
    public abstract Set<String> groupName();

    /**
     * 接口路径
     * @return 接口路径
     */
    @Nullable
    public abstract Set<String> path();

    /**
     * 角色信息
     * @return 角色信息
     */
    @NotNull
    public abstract SystemRole systemRole();
}
