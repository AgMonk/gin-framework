package init;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.DatabaseBackupService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 安装mysql client
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order(10000)
public class InitMysqlClient implements ApplicationRunner {
    private final DatabaseBackupService service;
    private final ObjectMapper objectMapper;

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        service.prepare();
        service.backup(true);
    }
}
