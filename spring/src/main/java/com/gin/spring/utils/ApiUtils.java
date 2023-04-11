package com.gin.spring.utils;

import com.gin.spring.annotation.MyRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/11 09:28
 */
public class ApiUtils {

    /**
     * 返回持有4种注解的元素
     * @param annotatedElement 元素
     * @return 接口方法
     */
    public static List<String> getApiPath(AnnotatedElement annotatedElement) {
        MyRestController a0 = annotatedElement.getAnnotation(MyRestController.class);
        if (a0 != null) {
            return Arrays.asList(a0.value());
        }
        RequestMapping a1 = annotatedElement.getAnnotation(RequestMapping.class);
        if (a1 != null) {
            return Arrays.asList(a1.value());
        }
        PostMapping a2 = annotatedElement.getAnnotation(PostMapping.class);
        if (a2 != null) {
            return Arrays.asList(a2.value());
        }
        GetMapping a3 = annotatedElement.getAnnotation(GetMapping.class);
        if (a3 != null) {
            return Arrays.asList(a3.value());
        }
        return new ArrayList<>();
    }
}
