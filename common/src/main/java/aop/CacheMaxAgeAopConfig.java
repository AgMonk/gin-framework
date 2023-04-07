package aop;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.WebUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;

/**
 * 响应AOP
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/22 14:23
 */
@Configuration
@Aspect
@Slf4j
@RequiredArgsConstructor
public class CacheMaxAgeAopConfig {
    @After("@annotation(cacheMaxAge)")
    public void after(CacheMaxAge cacheMaxAge) {
        final HttpServletResponse response = WebUtils.getHttpServletResponse();
        final CacheControl cacheControl = CacheControl.maxAge(cacheMaxAge.maxAge(), cacheMaxAge.timeUnit());
        assert response != null;
        response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl.getHeaderValue());

    }

}   
