package com.gin.security.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.security.bo.MyUserDetails;
import com.gin.security.entity.SystemUser;
import com.gin.security.interfaze.AuthorityProvider;
import com.gin.security.service.SystemUserService;
import com.gin.spring.utils.SpringContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
@Slf4j
public class WechatAuthenticationProvider implements AuthenticationProvider {
    private final SystemUserService systemUserService;
    private final WechatConfig wechatConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取openId
        final WechatAuthenticationToken token = (WechatAuthenticationToken) authentication;
        final String code = token.getOpenId();
        // 发送请求获取openId
        final String openId = wechatLoginRequest(code, wechatConfig).getOpenId();
        // 根据openId查询用户，如果不存在则注册
        final SystemUser user = systemUserService.findOrRegByOpenId(openId);

        //权限提供者提供的权限
        final Collection<AuthorityProvider> providers = SpringContextUtils.getContext().getBeansOfType(AuthorityProvider.class).values();
        final Set<GrantedAuthority> authorities = providers.stream().flatMap(provider -> provider.getAuthorities(user.getId()).stream()).collect(Collectors.toSet());

        // 构建用户details对象，放入token中
        final MyUserDetails userDetails = new MyUserDetails().with(user);
        userDetails.setAuthorities(authorities);

        // 返回一个已认证的Token
        final WechatAuthenticationToken newToken = WechatAuthenticationToken.authenticated(openId, userDetails, authorities);
        // 复制之前token的details
        newToken.setDetails(token.getDetails());
        return newToken;
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


    /**
     * 微信登录请求
     *
     * @param code         wx.login()获取的code
     * @param wechatConfig 微信配置
     * @return 请求详情
     */
    private static WechatLoginResponse wechatLoginRequest(String code, WechatConfig wechatConfig) {
        // 请求地址
        final String url = wechatConfig.obtainUrl(code);
        log.info("url: {}", url);
        // 发送登录请求
        final String result = new RestTemplate().getForObject(url, String.class);
        log.info(result);
        try {
            final WechatLoginResponse loginResponse = new ObjectMapper().readValue(result, WechatLoginResponse.class);
            if (loginResponse == null) {
                throw new AuthenticationServiceException("登录请求发送失败");
            }
            // 登录失败，报错
            if (loginResponse.getCode() != null && loginResponse.getCode() != 0) {
                log.warn(loginResponse.getErrMsg());
                throw new AuthenticationServiceException(String.format("%d: %s", loginResponse.getCode(), loginResponse.getErrMsg()));
            }
            return loginResponse;
        } catch (JsonProcessingException e) {
            throw new AuthenticationServiceException("响应解析失败");
        }
    }
}
