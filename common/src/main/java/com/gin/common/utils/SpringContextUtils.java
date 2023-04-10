package com.gin.common.utils;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 上下文工具
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:20
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;


    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
