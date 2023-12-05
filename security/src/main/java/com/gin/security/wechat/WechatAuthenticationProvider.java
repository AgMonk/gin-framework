package com.gin.security.wechat;

import com.gin.security.bo.MyUserDetails;
import com.gin.security.entity.SystemUser;
import com.gin.security.interfaze.AuthorityProvider;
import com.gin.security.service.SystemUserService;
import com.gin.spring.utils.SpringContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微信登录验证器
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/4 17:06
 **/
@Component
@RequiredArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {
    private final SystemUserService systemUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取openId
        String openId = ((WechatAuthenticationToken) authentication).getOpenId();
        // 用户
        final SystemUser user = systemUserService.findOrRegByOpenId(openId);

        //权限提供者提供的权限
        final Collection<AuthorityProvider> providers = SpringContextUtils.getContext().getBeansOfType(AuthorityProvider.class).values();
        final Set<GrantedAuthority> authorities = providers.stream().flatMap(provider -> provider.getAuthorities(user.getId()).stream()).collect(Collectors.toSet());

        // 构建用户details对象，放入token中
        final MyUserDetails details = new MyUserDetails().with(user);
        details.setAuthorities(authorities);

        // 返回一个已认证的Token
        return WechatAuthenticationToken.authenticated(openId, details, authorities);
    }

    /**
     * 判断当前验证器是否支持给定的 authentication
     *
     * @param authentication authentication
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
