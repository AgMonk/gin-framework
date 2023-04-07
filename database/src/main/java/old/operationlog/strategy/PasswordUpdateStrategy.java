package old.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.subclass.PasswordSubClass;
import org.springframework.stereotype.Component;

/**
 * 修改密码策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/24 14:50
 */
@Component
@LogStrategy(value = PasswordSubClass.class, type = OperationType.UPDATE)
public class PasswordUpdateStrategy implements DescriptionStrategy {

    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        return "修改密码";
    }
}
