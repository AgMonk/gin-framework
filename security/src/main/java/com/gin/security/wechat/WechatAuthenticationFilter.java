package com.gin.security.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.security.component.MyAuthenticationHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * 微信登录过滤器
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/4 14:25
 **/
@Slf4j
@Component
public class WechatAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 微信登录路径
     */
    public static final String WECHAT_LOGIN_PATH = "/wechat/login";
    /**
     * 允许的请求方法
     */
    public static final String METHOD = "POST";
    /**
     * 参数名称
     */
    public static final String PARAM_KEY = "code";
    /**
     * 路径匹配
     */
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(WECHAT_LOGIN_PATH, METHOD);

    WechatConfig wechatConfig;


    public WechatAuthenticationFilter(AuthenticationManager authenticationManager, MyAuthenticationHandler authenticationHandler, WechatConfig wechatConfig) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationFailureHandler(authenticationHandler);
        setAuthenticationSuccessHandler(authenticationHandler);

        this.wechatConfig = wechatConfig;

    }

    /**
     * 从请求中取出code参数，向微信服务器发送请求，如果成功获取到openID，则创建一个未认证的 WechatAuthenticationToken ， 并将其提交给 AuthenticationManager
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!METHOD.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        final String code = request.getParameter(PARAM_KEY);
        if (ObjectUtils.isEmpty(code)) {
            throw new AuthenticationServiceException("code 不允许为空");
        }
        final String openId = weChatLoginRequest(code, wechatConfig).getOpenId();
//        final String openId = "open_Id_test";
        // 登录成功，使用openId创建一个Token
        final WechatAuthenticationToken token = WechatAuthenticationToken.unauthenticated(openId);
        log.info("openId: {}", openId);
        // 设置details
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        // 将token提交给 AuthenticationManager
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * 微信登录请求
     *
     * @param code         wx.login()获取的code
     * @param wechatConfig 微信配置
     * @return 请求详情
     * @throws JsonProcessingException 异常
     */
    private static WechatLoginResponse weChatLoginRequest(String code, WechatConfig wechatConfig) throws JsonProcessingException {
        // 请求地址
        final String url = wechatConfig.obtainUrl(code);
        log.info("url: {}", url);
        // 发送登录请求
        final String result = new RestTemplate().getForObject(url, String.class);
        log.info(result);
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
    }
}
