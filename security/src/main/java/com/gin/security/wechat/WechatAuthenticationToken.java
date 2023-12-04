package com.gin.security.wechat;

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
public class WechatAuthenticationToken extends AbstractAuthenticationToken {
    private final String openId;

    /**
     * 未认证的Token
     *
     * @param openId openId
     */
    public WechatAuthenticationToken(String openId) {
        super(null);
        this.openId = openId;
        setAuthenticated(false);
    }

    /**
     * 已认证的token
     *
     * @param openId      openId
     * @param authorities 权限
     */
    public WechatAuthenticationToken(String openId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openId = openId;
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
    public static WechatAuthenticationToken authenticated(String openId, Collection<? extends GrantedAuthority> authorities) {
        return new WechatAuthenticationToken(openId, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return openId;
    }

}
