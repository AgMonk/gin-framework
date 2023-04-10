package route.strategy;

import org.springframework.stereotype.Component;
import route.annotation.MenuItem;
import route.entity.EleMenuItem;

import java.lang.reflect.Method;

/**
 * 总是显示(默认策略)
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 10:04
 */
@Component
public class AlwaysFalseStrategy implements VisibleStrategy {

    /**
     * 判断{@link  EleMenuItem} 的disabled字段
     * @param menuItem Controller上的注解
     * @param method   请求方法
     * @return 是否disabled
     */
    @Override
    public boolean isDisable(MenuItem menuItem, Method method, String requestUrl) {
        return false;
    }
}
