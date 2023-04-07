package old.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.OperationLogContext;

/**
 * 描述生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 11:59
 */
public interface DescriptionStrategy {
    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    String generateDescription(OperationLogContext context);
}
