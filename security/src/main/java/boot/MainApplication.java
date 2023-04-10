package boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 15:31
 */
@SpringBootApplication
@MapperScan(basePackages = {"dao"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
