package service;

import base.BaseFields;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dto.form.RelationUserRoleForm;
import entity.RelationUserRole;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public interface RelationUserRoleService extends MyService<RelationUserRole> {
    org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(RelationUserRoleService.class);

    /**
     * 为指定用户添加角色
     * @param userId 用户id
     * @param params 参数
     * @return 添加好的角色
     */
    default List<RelationUserRole> add(long userId, Collection<RelationUserRoleForm> params) {
        if (CollectionUtils.isEmpty(params)) {
            return new ArrayList<>();
        }
        final List<RelationUserRole> userRoles = new ArrayList<>(params.stream().map(i -> i.build(userId)).toList());
        //去重
        userRoles.removeAll(listByUserId(Collections.singleton(userId)));
        if (userRoles.size() > 0) {
            LOG.info("为账号id = {} 添加 {} 个角色", userId, userRoles.size());
            saveBatch(userRoles);
        }
        return userRoles;
    }

    /**
     * 为指定用户配置角色
     * @param userId 用户id
     * @param params 参数
     * @return 配置完毕的角色
     */
    default List<RelationUserRole> config(long userId, Collection<RelationUserRoleForm> params) {
        // 查询指定用户持有的角色id
        //已有数据 (含有id)
        final List<RelationUserRole> oldData = listByUserId(Collections.singleton(userId));
        //新数据 (不含id)
        final List<RelationUserRole> newData = new ArrayList<>(params.stream().map(i -> i.build(userId)).toList());

        //过滤出不存在的，进行删除
        final List<RelationUserRole> data2Del = oldData.stream().filter(o -> !newData.contains(o)).toList();
        if (data2Del.size() > 0) {
            removeBatchByIds(data2Del.stream().map(BaseFields::getId).collect(Collectors.toList()));
            oldData.removeAll(data2Del);
        }

        //过滤出新增的，进行添加
        final List<RelationUserRole> data2Add = newData.stream().filter(o -> !oldData.contains(o)).toList();
        if (data2Add.size() > 0) {
            saveBatch(data2Add);
            newData.removeAll(data2Add);
        }

        //过滤出已经存在的，进行修改
        if (newData.size() > 0) {
            final long now = System.currentTimeMillis() / 1000;
            final List<RelationUserRole> data2Update = newData.stream().peek(nd -> {
                // 补充id
                nd.setId(oldData.stream().filter(od -> od.equals(nd)).toList().get(0).getId());
                // 设置修改时间
                nd.setTimeUpdate(now);
            }).toList();
            updateBatchById(data2Update);
        }

        final List<RelationUserRole> res = new ArrayList<>();
        res.addAll(data2Add);
        res.addAll(newData);
        res.sort((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));
        return res;
    }

    /**
     * 为指定用户删除角色
     * @param userId  用户id
     * @param roleIds 角色id
     */
    default void del(long userId, Collection<Long> roleIds) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("role_id", roleIds).eq("user_id", userId);
        remove(qw);
    }

    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return 用户持有的角色
     */
    default List<RelationUserRole> listByUserId(Collection<Long> userId) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("user_id", userId);
        return list(qw);
    }

    /**
     * 查询持有指定角色的用户id
     * @param roleIds 角色id
     * @return 用户id
     */
    default List<Long> listUserIdByRoleId(Collection<Long> roleIds) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("role_id", roleIds).select("user_id");
        return list(qw).stream().map(RelationUserRole::getUserId).toList();
    }
}