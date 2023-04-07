package init;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化任务模板
 * @author bx002
 */
@Component
@Getter
@Slf4j
@RequiredArgsConstructor
@Order
public class InitCompleted implements ApplicationRunner {

    /**
     * 任务内容
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("----------启动完成----------");
    }
}
