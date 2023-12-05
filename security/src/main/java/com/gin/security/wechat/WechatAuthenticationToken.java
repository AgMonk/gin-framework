package com.gin.security.wechat;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信登录认证Token
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/4 15:12
 **/
@Getter
public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    private final String openId;
    private final Object principal;

    /**
     * 未认证的Token
     *
     * @param openId openId
     */
    public WechatAuthenticationToken(String openId) {
        super(null);
        this.openId = openId;
        this.principal = openId;
        setAuthenticated(false);
    }

    /**
     * 已认证的token
     *
     * @param openId      openId
     * @param authorities 权限
     */
    public WechatAuthenticationToken(String openId, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openId = openId;
        this.principal = principal;
        setAuthenticated(true);
    }

    /**
     * 未认证的Token
     *
     * @param openId openId
     */
    public static WechatAuthenticationToken unauthenticated(String openId) {
        return new WechatAuthenticationToken(openId);
    }

    /**
     * 已认证的token
     *
     * @param openId      openId
     * @param authorities 权限
     */
    public static WechatAuthenticationToken authenticated(String openId, Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new WechatAuthenticationToken(openId, principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
