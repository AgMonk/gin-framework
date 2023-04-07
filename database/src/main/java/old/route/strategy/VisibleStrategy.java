package old.route.strategy;

import com.gin.springboot3template.route.annotation.MenuItem;

import java.lang.reflect.Method;

/**
 * 路由导航展示策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 09:59
 */
public interface VisibleStrategy {
    /**
     * 判断{@link  com.gin.springboot3template.route.entity.EleMenuItem} 的disabled字段
     * @param menuItem   Controller上的注解
     * @param method     请求方法
     * @param requestUrl 请求地址
     * @return 是否disabled
     */
    boolean isDisable(MenuItem menuItem, Method method, String requestUrl);
}
