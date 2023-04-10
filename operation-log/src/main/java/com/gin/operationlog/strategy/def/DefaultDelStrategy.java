package com.gin.operationlog.strategy.def;

import com.gin.operationlog.annotation.LogStrategy;
import com.gin.operationlog.bo.OperationLogContext;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.strategy.DescriptionStrategy;
import org.springframework.stereotype.Component;

/**
 * 默认删除策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 09:18
 */
@Component
@LogStrategy(type = OperationType.DEL)
public class DefaultDelStrategy implements DescriptionStrategy {

    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        return "";
    }
}
