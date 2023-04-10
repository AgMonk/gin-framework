package old.user.security.handler;

import com.gin.springboot3template.user.security.service.PermissionEvaluatorProxyService;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

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
