package strategy;

import annotation.LogStrategy;
import bo.OperationLogContext;
import enums.OperationType;
import org.springframework.stereotype.Component;
import subclass.PasswordSubClass;

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
