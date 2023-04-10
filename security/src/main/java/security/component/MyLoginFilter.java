package security.component;

import Constant.Security;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ObjectUtils;
import service.CaptchaService;

import java.io.IOException;
import java.util.Map;

import static Constant.Security.APPLICATION_JSON_CHARSET_UTF_8;
import static Constant.Security.VERIFY_CODE_KEY;


/**
 * 登陆过滤
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 15:40
 */
@Component
public class MyLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CaptchaService captchaService;

    public MyLoginFilter(
            AuthenticationManager authenticationManager,
            MyAuthenticationHandler authenticationHandler,
            MyRememberMeServices rememberMeServices,
            CaptchaService captchaService
    ) {
        super(authenticationManager);
        this.captchaService = captchaService;
        setAuthenticationFailureHandler(authenticationHandler);
        setAuthenticationSuccessHandler(authenticationHandler);
        //rememberMe
        setRememberMeServices(rememberMeServices);
        //登陆使用的路径
        setFilterProcessesUrl("/sys/user/login");
    }

    private static boolean isContentTypeJson(@SuppressWarnings("unused") HttpServletRequest request) {
        final String contentType = request.getContentType();
        return APPLICATION_JSON_CHARSET_UTF_8.equalsIgnoreCase(contentType) || MimeTypeUtils.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = null;
        String password = null;
        String verifyCode = null;
        String rememberMe = null;
        if (isContentTypeJson(request)) {
            try {
                Map<String, String> map = objectMapper.readValue(request.getInputStream(), new TypeReference<>() {
                });
                username = map.get(getUsernameParameter());
                password = map.get(getPasswordParameter());
                verifyCode = map.get(VERIFY_CODE_KEY);
                rememberMe = map.get(Security.REMEMBER_ME_KEY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            username = obtainUsername(request);
            password = obtainPassword(request);
            verifyCode = request.getParameter(VERIFY_CODE_KEY);
            rememberMe = request.getParameter(Security.REMEMBER_ME_KEY);
        }
        //校验验证码
        captchaService.validate(request.getSession().getId(), verifyCode);

        //将 rememberMe 状态存入 attr中
        if (!ObjectUtils.isEmpty(rememberMe)) {
            request.setAttribute(Security.REMEMBER_ME_KEY, rememberMe);
        }

        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


}
