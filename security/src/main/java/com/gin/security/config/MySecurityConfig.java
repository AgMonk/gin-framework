package com.gin.security.config;


import com.gin.databasebackup.properties.DatabaseProperties;
import com.gin.security.Constant.Security;
import com.gin.security.wechat.WechatAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import com.gin.security.component.MyAuthenticationHandler;
import com.gin.security.component.MyLoginFilter;
import com.gin.security.component.MyRememberMeServices;
import com.gin.security.service.MyUserDetailsServiceImpl;

import javax.sql.DataSource;
import java.util.List;

/**
 * SpringSecurity配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:53
 */
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class MySecurityConfig {
    /**
     * 接口文档放行
     */
    private static final List<String> DOC_WHITE_LIST = List.of("/doc.html", "/webjars/**", "/v3/api-docs/**");
    /**
     * 测试接口放行
     */
    private static final List<String> TEST_WHITE_LIST = List.of("/test/**");
    /**
     * 白名单:注册
     */
    private static final List<String> WHITE_LIST = List.of("/sys/user/reg", "/test/**");
    /**
     * 验证码放行
     */
    private static final List<String> VERIFY_CODE_WHITE_LIST = List.of("/sys/verifyCode/**");

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 允许抛出用户不存在的异常
     * @param myUserDetailsService myUserDetailsService
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(MyUserDetailsServiceImpl myUserDetailsService) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setUserDetailsPasswordService(myUserDetailsService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * 自定义RememberMe服务token持久化仓库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource datasource, DatabaseProperties  properties) {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(datasource);
        //第一次启动的时候建表
        if (properties.isInitRememberMe()) {
            tokenRepository.setCreateTableOnStartup(true);
        }
        return tokenRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            MyLoginFilter loginFilter,
            WechatAuthenticationFilter weChatAuthenticationFilter,
            MyAuthenticationHandler authenticationHandler,
            MyRememberMeServices rememberMeServices
    ) throws Exception {
        //路径配置
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, DOC_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, VERIFY_CODE_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(TEST_WHITE_LIST.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
        ;
        // 微信登录
        http.addFilterBefore(weChatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //登陆
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //登出
        http.logout().logoutUrl(Security.LOGOUT_URI).logoutSuccessHandler(authenticationHandler);

        //禁用 csrf
//        http.csrf().disable();

        //csrf验证 存储到Cookie中
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        ;

        //会话管理
        http.sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(authenticationHandler)
        //引入redis-session依赖后已不再需要手动配置 sessionRegistry
//                .sessionRegistry(new SpringSessionBackedSessionRegistry<>(new RedisIndexedSessionRepository(RedisConfig.createRedisTemplate())))
        //禁止后登陆挤下线
//               .maxSessionsPreventsLogin(true)
        ;

        //rememberMe
        http.rememberMe().rememberMeServices(rememberMeServices);

        // 权限不足时的处理
        http.exceptionHandling()
                .accessDeniedHandler(authenticationHandler)
                .authenticationEntryPoint(authenticationHandler)
        ;


        return http.build();
    }
}
