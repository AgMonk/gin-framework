package com.gin.common.utils;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 网络工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 11:25
 */
public class WebUtils {
    public static final String UN_KNOWN = "unKnown";

    @Nullable
    public static HttpServletRequest getHttpServletRequest() {
        final ServletRequestAttributes attributes = getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    @Nullable
    public static HttpServletResponse getHttpServletResponse() {
        final ServletRequestAttributes attributes = getRequestAttributes();
        return attributes == null ? null : attributes.getResponse();
    }

    /**
     * 获取目标主机的ip
     */
    public static String getRemoteHost(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");
        if (!ObjectUtils.isEmpty(xFor) && !UN_KNOWN.equalsIgnoreCase(xFor)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xip;
        if (!ObjectUtils.isEmpty(xFor) && !UN_KNOWN.equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (ObjectUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (ObjectUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ObjectUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ObjectUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ObjectUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }

        return xFor;
    }

    /**
     * 获取目标主机的ip
     */
    public static String getRemoteHost() {
        ServletRequestAttributes sra = getRequestAttributes();
        if (sra != null) {
            return getRemoteHost(sra.getRequest());
        }
        return null;
    }

    @Nullable
    private static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
}   
