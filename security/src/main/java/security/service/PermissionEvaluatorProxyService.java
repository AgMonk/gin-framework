package security.service;


import Constant.Role;
import exception.AuthorityEvaluatorDuplicatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import security.bo.MyUserDetails;
import security.interfaze.ClassAuthorityEvaluator;
import security.interfaze.TypeNameAuthorityEvaluator;
import service.SystemPermissionService;
import utils.SpringContextUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 权限评估代理
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 13:31
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionEvaluatorProxyService implements PermissionEvaluator {
    private final SystemPermissionService systemPermissionService;

    /**
     * 通过class来选择 AuthorityEvaluator 的Map
     */
    HashMap<Class<?>, ClassAuthorityEvaluator> classMap;
    /**
     * 通过name来选择 AuthorityEvaluator 的Map
     */
    HashMap<String, TypeNameAuthorityEvaluator> nameMap;

    public ClassAuthorityEvaluator getClassAuthorityEvaluator(Class<?> clazz) {
        if (this.classMap == null) {
            HashMap<Class<?>, ClassAuthorityEvaluator> map = new HashMap<>(1);
            final Collection<ClassAuthorityEvaluator> authorityEvaluators = SpringContextUtils.getContext().getBeansOfType(ClassAuthorityEvaluator.class).values();
            for (ClassAuthorityEvaluator authorityEvaluator : authorityEvaluators) {
                final List<Class<?>> targetClass = authorityEvaluator.getTargetClass();
                for (Class<?> c : targetClass) {
                    if (!map.containsKey(c)) {
                        map.put(c, authorityEvaluator);
                    } else {
                        throw new AuthorityEvaluatorDuplicatedException("类:" + c.getSimpleName());
                    }
                }
            }
            this.classMap = map;
        }
        return this.classMap.getOrDefault(clazz, null);
    }

    public TypeNameAuthorityEvaluator getTypeNameAuthorityEvaluator(String targetType) {
        if (this.nameMap == null) {
            HashMap<String, TypeNameAuthorityEvaluator> map = new HashMap<>(1);
            final Collection<TypeNameAuthorityEvaluator> authorityEvaluators = SpringContextUtils.getContext().getBeansOfType(
                    TypeNameAuthorityEvaluator.class).values();
            for (TypeNameAuthorityEvaluator authorityEvaluator : authorityEvaluators) {
                final List<String> targetTypes = authorityEvaluator.getTargetTypes();
                for (String type : targetTypes) {
                    if (!map.containsKey(type)) {
                        map.put(type, authorityEvaluator);
                    } else {
                        throw new AuthorityEvaluatorDuplicatedException(type);
                    }
                }
            }
            this.nameMap = map;
        }
        return this.nameMap.getOrDefault(targetType, null);
    }

    /**
     * 判断指定用户对指定类型和ID的资源持有指定权限
     * @param authentication 用户认证信息
     * @param targetId       目标资源id
     * @param targetType     目标资源类型
     * @param permission     权限
     * @return 是否持有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        log.debug("检查权限: {} {} {}", targetId, targetType, permission);
        if (targetId == null) {
            return false;
        }
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        //如果持有 admin 角色 ，直接放行
        if (myUserDetails.hasRole(Role.ADMIN)) {
            return true;
        }
        //从 nameMap 中选择  权限评估器
        final TypeNameAuthorityEvaluator authorityEvaluator = getTypeNameAuthorityEvaluator(targetType);
        if (authorityEvaluator == null) {
            log.warn("没有负责该类型的权限评估器: " + targetType);
            return false;
        }
        return authorityEvaluator.hasPermission(myUserDetails, targetId, permission);
    }

    /**
     * 判断指定用户对指定资源持有指定权限
     * @param authentication     用户认证信息
     * @param targetDomainObject 判断的资源
     * @param permission         权限
     * @return 是否持有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        log.debug("检查权限: {} {}", targetDomainObject, permission);
        if (targetDomainObject == null) {
            return false;
        }
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        //如果持有 admin 角色 ，直接放行
        if (myUserDetails.hasRole(Role.ADMIN)) {
            return true;
        }
        //从 classMap 中选择  权限评估器
        final ClassAuthorityEvaluator authorityEvaluator = getClassAuthorityEvaluator(targetDomainObject.getClass());
        if (authorityEvaluator == null) {
            log.warn("没有负责该 Class 的权限评估器: " + targetDomainObject.getClass());
            return false;
        }
        return authorityEvaluator.hasPermission(myUserDetails, targetDomainObject, permission);
    }
}
