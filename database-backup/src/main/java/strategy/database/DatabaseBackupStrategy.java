package strategy.database;

import annotation.LogStrategy;
import bo.OperationLogContext;
import enums.OperationType;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;
import strategy.DescriptionStrategy;
import vo.FileInfo;

import java.util.List;

/**
 * 数据库备份策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 10:26
 */
@Component
@LogStrategy(value = Database.class, type = OperationType.BACKUP)
public class DatabaseBackupStrategy implements DescriptionStrategy {
    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        final List<Object> sufExp = context.sufExp();
        if (sufExp.size() > 0 && sufExp.get(0) instanceof FileInfo fileInfo) {
            return fileInfo.toString();
        }
        return null;
    }
}
