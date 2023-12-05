package com.gin.security.wechat;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 微信登录响应对象
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/4 15:45
 **/
@Getter
@Setter
public class WechatLoginResponse {
    /**
     * 会话密钥
     */
    @JsonAlias("session_key")
    String sessionKey;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见<a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html"> UnionID 机制说明</a>。
     */
    @JsonAlias("unionid")
    String unionId;
    /**
     * 错误信息
     */
    @JsonAlias("errmsg")
    String errMsg;
    /**
     * 用户唯一标识
     */
    @JsonAlias("openid")
    String openId;
    /**
     * 错误码
     */
    @JsonAlias("errcode")
    Integer code;
}
