package old.route.controller;

import com.gin.springboot3template.route.base.EleMenuComponent;
import com.gin.springboot3template.route.service.MenuService;
import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.config.redis.CustomKeyGenerator;
import com.gin.springboot3template.sys.config.redis.RedisConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 前端路由
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 15:33
 */

@MyRestController(RouteMenuController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = RouteMenuController.GROUP_NAME)
@Slf4j
@CacheConfig(cacheManager = RedisConfig.REDIS_CACHE_MANAGER, keyGenerator = CustomKeyGenerator.NAME, cacheNames = RouteMenuController.CACHE_NAMES)
public class RouteMenuController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/route";
    public static final String CACHE_NAMES = "old/route";
    public static final String GROUP_NAME = "路由菜单接口";

    private final MenuService service;

    @GetMapping("menu")
    @Operation(summary = "获取路由菜单", description = "根据当前用户的权限, 返回一个该用户可以访问的路由结构, 超管执行该请求时将显示所有路由。"
            + "<br/>响应数据可以直接使用<a href='https://element-plus.gitee.io/zh-CN/component/menu.html' target='_blank'>Element的Menu组件</a>进行渲染" + "<br/>如果选择使用本接口, 请按照接口返回的地址来定义路由地址")
    public List<EleMenuComponent> getMenu(@RequestParam(required = false, defaultValue = "index") String name, HttpSession session) {
        return service.listItemByMenuName(name);
    }

    @GetMapping("names")
    @Operation(summary = "获取菜单名称列表")
    public List<String> getMenuNames() {
        return service.listMenuNames();
    }
}