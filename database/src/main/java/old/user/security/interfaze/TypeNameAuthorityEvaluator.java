package old.user.security.interfaze;

import com.gin.springboot3template.user.security.bo.MyUserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 按 类型名称 匹配的 权限评估器
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 13:52
 */
public interface TypeNameAuthorityEvaluator {
    /**
     * 用于向 PermissionEvaluatorProxyService 注册，不能重复
     * @return 本类所管理的资源类型名称
     */
    default List<String> getTargetTypes() {
        return new ArrayList<>();
    }

    /**
     * 判断用户是否对指定id的资源有指定权限
     * @param userDetails 用户
     * @param targetId    资源id
     * @param permission  权限
     * @return 是否有权限
     */
    default boolean hasPermission(MyUserDetails userDetails, Serializable targetId, Object permission) {
        return false;
    }
}
