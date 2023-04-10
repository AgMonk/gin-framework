package com.gin.database.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gin.common.utils.SpringContextUtils;
import org.springframework.context.ApplicationContext;

/**
 * 服务工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/7 17:31
 */
public class ServiceUtils {
    /**
     * 根据指定的泛型类型 查找对应的 IService Bean
     * @param entityClass 泛型类型
     * @param <T>         泛型类型
     * @return IService Bean
     */
    public static <T> IService<T> findService(Class<T> entityClass) {
        final ApplicationContext context = SpringContextUtils.getContext();
        if (context == null) {
            return null;
        }
        //noinspection unchecked
        return context.getBeansOfType(IService.class).values().stream()
                .filter(i -> i.getEntityClass().equals(entityClass))
                .findAny()
                .orElse(null);
    }
}   
