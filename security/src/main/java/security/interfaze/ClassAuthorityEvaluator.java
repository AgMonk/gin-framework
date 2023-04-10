package security.interfaze;


import security.bo.MyUserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * 按 Class 匹配的 权限评估器
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 13:52
 */
public interface ClassAuthorityEvaluator {
    /**
     * 用于向 PermissionEvaluatorProxyService 注册，不能重复
     * @return 本类所管理的资源class
     */
    default List<Class<?>> getTargetClass() {
        return new ArrayList<>();
    }

    /**
     * 判断用户是否对指定资源有指定权限
     * @param userDetails        用户
     * @param targetDomainObject 资源
     * @param permission         权限
     * @return 是否有权限
     */
    default boolean hasPermission(MyUserDetails userDetails, Object targetDomainObject, Object permission) {
        return false;
    }

}
