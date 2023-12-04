package com.gin.security.wechat;

import com.gin.route.annotation.MenuItem;
import com.gin.spring.annotation.MyRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信接口
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/4 16:24
 **/
@Tag(name = WechatController.GROUP_NAME)
@MyRestController(WechatController.API_PREFIX)
@MenuItem(title = "微信接口", description = "微信接口")
public class WechatController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/wechat";
    public static final String GROUP_NAME = "微信接口";

    @PostMapping("login")
    @Operation(summary = "微信登录")
    public void post(@RequestParam @Parameter(description = "由wx.login()方法获取的code") String code) {
    }
}
