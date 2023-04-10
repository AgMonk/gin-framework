package com.gin.security.handler;


import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;
import com.gin.security.service.PermissionEvaluatorProxyService;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 11:08
 */
@Component
public class MyMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    public MyMethodSecurityExpressionHandler(PermissionEvaluatorProxyService evaluatorProxyService) {
        setPermissionEvaluator(evaluatorProxyService);
    }

}
