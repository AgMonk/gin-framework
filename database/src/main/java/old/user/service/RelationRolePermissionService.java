package old.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.base.BasePo;
import com.gin.springboot3template.sys.service.MyService;
import com.gin.springboot3template.user.entity.RelationRolePermission;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface RelationRolePermissionService extends MyService<RelationRolePermission> {
    org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(RelationUserRoleService.class);

    /**
     * 将角色id 和权限id 组装成 角色权限列表
     * @param roleId  角色id
     * @param permIds 权限id
     * @return 角色权限列表
     */
    @NotNull
    private static List<RelationRolePermission> build(long roleId, Collection<Long> permIds) {
        return permIds.stream().map(pId -> {
            final RelationRolePermission permission = new RelationRolePermission();
            permission.setRoleId(roleId);
            permission.setPermissionId(pId);
            return permission;
        }).toList();
    }

    /**
     * 为指定角色添加权限
     * @param roleId  角色id
     * @param permIds 权限id
     * @return 添加好的权限
     */
    default List<RelationRolePermission> add(long roleId, Collection<Long> permIds) {
        final List<RelationRolePermission> rolePermissions = new ArrayList<>(build(roleId, permIds));
        //去重
        rolePermissions.removeAll(listByRoleId(Collections.singleton(roleId)));
        if (rolePermissions.size() > 0) {
            LOG.info("为角色id = {} 添加 {} 个权限", roleId, rolePermissions.size());
            saveBatch(rolePermissions);
        }
        return rolePermissions;
    }

    /**
     * 为指定角色配置权限
     * @param roleId  角色id
     * @param permIds 权限id
     * @return 新数据
     */
    default List<RelationRolePermission> config(long roleId, Collection<Long> permIds) {
        //已有数据 (含有id)
        final List<RelationRolePermission> oldData = listByRoleId(Collections.singleton(roleId));
        //新数据 (不含id)
        final List<RelationRolePermission> newData = build(roleId, permIds);


        //过滤出不存在的，进行删除
        final List<RelationRolePermission> data2Del = oldData.stream().filter(o -> !newData.contains(o)).toList();
        if (data2Del.size() > 0) {
            removeBatchByIds(data2Del.stream().map(BasePo::getId).collect(Collectors.toList()));
        }

        //过滤出新增的，进行添加
        final List<RelationRolePermission> data2Add = newData.stream().filter(o -> !oldData.contains(o)).toList();
        if (data2Add.size() > 0) {
            saveBatch(data2Add);
        }

        return newData;

    }

    /**
     * 为指定角色删除权限
     * @param roleId  角色id
     * @param permIds 权限id
     * @return 移除的权限
     */
    default List<RelationRolePermission> del(long roleId, Collection<Long> permIds) {
        final QueryWrapper<RelationRolePermission> qw = new QueryWrapper<>();
        qw.eq("role_id", roleId).in("permission_id", permIds);
        final List<RelationRolePermission> res = list(qw);
        remove(qw);
        return res;
    }

    /**
     * 根据角色Id 查询其持有的权限
     * @param roleId 角色id
     * @return 权限
     */
    default List<RelationRolePermission> listByRoleId(Collection<Long> roleId) {
        final QueryWrapper<RelationRolePermission> qw = new QueryWrapper<>();
        qw.in("role_id", roleId);
        return list(qw);
    }
}