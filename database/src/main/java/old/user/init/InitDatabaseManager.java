package old.user.init;

import com.gin.springboot3template.databsebackup.controller.DatabaseController;
import com.gin.springboot3template.user.entity.SystemRole;
import com.gin.springboot3template.user.service.RolePermissionService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 初始化标签管理员
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/9 09:43
 */
@Component
@Order(10)
public class InitDatabaseManager extends InitModuleManager {
    public InitDatabaseManager(RolePermissionService rolePermissionService) {
        super(rolePermissionService);
    }

    @Nullable
    @Override
    public Set<String> groupName() {
        return Set.of(DatabaseController.GROUP_NAME);
    }

    @Nullable
    @Override
    public Set<String> path() {
        return null;
    }

    @NotNull
    @Override
    public SystemRole systemRole() {
        final SystemRole role = new SystemRole();
        role.setName("database_manager");
        role.setNameZh("数据库管理员");
        role.setRemark("系统自动创建,禁止修改");
        return role;
    }
}
