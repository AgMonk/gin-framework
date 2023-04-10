package service;

import entity.SystemOperationLog;
import jakarta.validation.constraints.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:02
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemOperationLogService extends OperationLogService<SystemOperationLog> {

    /**
     * 写入日志
     * @param log 日志
     */
    @Async
    default void write(@NotNull Collection<SystemOperationLog> log) {
        if (log.size() == 0) {
            return;
        }
        saveBatch(log);
    }

    /**
     * 写入日志
     * @param log 日志
     */
    @Async
    default void write(@NotNull SystemOperationLog log) {
        save(log);
    }
}