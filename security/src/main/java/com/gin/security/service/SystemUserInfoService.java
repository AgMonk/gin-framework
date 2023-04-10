package com.gin.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.database.service.MyService;
import com.gin.operationlog.service.UserInfoService;
import com.gin.security.dto.form.SystemUserInfoForm;
import com.gin.security.entity.SystemUserInfo;
import com.gin.security.utils.MySecurityUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:44
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemUserInfoService extends MyService<SystemUserInfo>, UserInfoService {
    /**
     * 根据用户名查询用户个人信息
     * @param userId 用户id
     * @return 个人信息
     */
    default SystemUserInfo getByUserId(long userId) {
        final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return getOne(qw);
    }

    /**
     * 查询 userId 到昵称的映射
     * @param userId 用户id
     * @return dic
     */
    @Override
    default HashMap<Long, String> getIdNameMap(Collection<Long> userId) {
        final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
        qw.select("user_id", "nickname").in("user_id", userId);
        final List<SystemUserInfo> list = list(qw);
        final HashMap<Long, String> map = new HashMap<>(list.size());
        list.forEach(i -> map.put(i.getUserId(), i.getNickname()));
        return map;
    }

    /**
     * 返回当前登陆用户的用户id
     * @return 用户id
     */
    @Override
    default Long getCurrentUserId() {
        return MySecurityUtils.currentUserDetails().getId();
    }

    /**
     * 保存或更新用户个人信息
     * @param userId 用户id
     * @param param  参数
     * @return 保存或更新的用户个人信息
     */
    default SystemUserInfo saveOrUpdate(Long userId, SystemUserInfoForm param) {
        final SystemUserInfo build = param.build(userId);
        final SystemUserInfo userInfo = getByUserId(userId);
        if (userInfo == null) {
            //不存在用户信息 添加
            save(build);
        } else {
            build.setId(userInfo.getId());
            // 已存在用户信息 修改
            updateById(build);
        }
        return build;
    }
}