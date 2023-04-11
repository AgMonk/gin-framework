package com.gin.security.aop;

import com.gin.spring.utils.WebUtils;
import com.gin.operationlog.entity.SystemOperationLog;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.service.SystemOperationLogService;
import com.gin.security.bo.MyUserDetails;
import com.gin.security.entity.SystemUser;
import com.gin.security.service.SystemUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * AOP
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 14:43
 */
@Configuration
@Aspect
@Slf4j
@RequiredArgsConstructor
public class SecurityAopConfig {
    private final SystemOperationLogService logService;
    private final SystemUserService systemUserService;
    private static long now() {
        return System.currentTimeMillis();
    }
    /**
     * 登陆方法环绕切面
     * @param pjp pjp
     * @return 返回
     */
    @Around(value = "execution(* org.springframework.security.authentication.AuthenticationManager.authenticate(..)) && args(token)", argNames = "pjp,token")
    public Object login(ProceedingJoinPoint pjp, UsernamePasswordAuthenticationToken token) throws Throwable {
        if (ProviderManager.class.equals(pjp.getTarget().getClass())
                && token.getDetails() instanceof WebAuthenticationDetails details
        ) {
            final long start = now();
            final String userIp = details.getRemoteAddress();
            try {
                //登陆成功
                final Authentication result = (Authentication) pjp.proceed();
                if (result.isAuthenticated()) {
                    MyUserDetails userDetails = (MyUserDetails) result.getPrincipal();
                    final SystemOperationLog operationLog = new SystemOperationLog();
                    final Long userId = userDetails.getId();
                    operationLog.setMainClass(SystemUser.class);
                    operationLog.setSessionId(details.getSessionId());
                    operationLog.setType(OperationType.LOGIN);
                    operationLog.setUserIp(userIp);
                    operationLog.setUserId(userId);
                    operationLog.setMainId(userId);
                    operationLog.setDescription("登录成功");
                    operationLog.setTimeCost(now() - start);
                    logService.write(operationLog);
                }
                return result;
            } catch (Throwable e) {
                // 登陆失败
                final SystemUser user = systemUserService.getByUsername((String) token.getPrincipal());
                final long userId = user != null ? user.getId() : -1;
                final SystemOperationLog operationLog = new SystemOperationLog();
                operationLog.setMainClass(SystemUser.class);
                operationLog.setSessionId(details.getSessionId());
                operationLog.setType(OperationType.LOGIN_FAILED);
                operationLog.setUserIp(userIp);
                operationLog.setUserId(userId);
                operationLog.setMainId(userId);
                operationLog.setDescription(e.getLocalizedMessage());
                operationLog.setTimeCost(now() - start);
                logService.write(operationLog);
                throw e;
            }
        }
        return pjp.proceed();
    }

    @Around(value = "execution(* org.springframework.security.web.authentication.logout.LogoutSuccessHandler.onLogoutSuccess(..)) && args(request,response,token)"
            , argNames = "pjp,request,response,token")
    public Object logout(
            ProceedingJoinPoint pjp,
            HttpServletRequest request,
            HttpServletResponse response,
            UsernamePasswordAuthenticationToken token
    ) throws Throwable {
        if (token.getDetails() instanceof WebAuthenticationDetails details) {
            final long start = now();
            MyUserDetails userDetails = (MyUserDetails) token.getPrincipal();
            final Long userId = userDetails.getId();

            final Object result = pjp.proceed();

            final SystemOperationLog operationLog = new SystemOperationLog();
            operationLog.setMainClass(SystemUser.class);
            operationLog.setSessionId(details.getSessionId());
            operationLog.setType(OperationType.LOGOUT);
            operationLog.setUserIp(WebUtils.getRemoteHost(request));
            operationLog.setUserId(userId);
            operationLog.setMainId(userId);
            operationLog.setDescription("登出成功");
            operationLog.setTimeCost(now() - start);
            logService.write(operationLog);
            return result;
        }
        return pjp.proceed();
    }
}   
