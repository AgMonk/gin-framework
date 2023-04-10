package strategy.database;

import annotation.LogStrategy;
import bo.OperationLogContext;
import enums.OperationType;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;
import service.DatabaseBackupService;
import strategy.DescriptionStrategy;
import com.gin.common.vo.FileInfo;

/**
 * 数据库备份下载策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 10:26
 */
@Component
@LogStrategy(value = Database.class, type = OperationType.DOWNLOAD)
@RequiredArgsConstructor
public class DatabaseDownloadStrategy implements DescriptionStrategy {
    public static final String FILENAME = "filename";
    private final DatabaseBackupService service;

    @Nullable
    public static String getFilename(OperationLogContext context) {
        return context.paramArgs().stream()
                .filter(pa -> FILENAME.equals(pa.parameter().getName()))
                .filter(pa -> String.class.equals(pa.parameter().getType()))
                .map(pa -> (String) pa.arg()).findFirst().orElse(null);
    }

    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        return new FileInfo(service.getBackupFile(getFilename(context))).toString();
    }
}
