package com.gin.jackson.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/6/21 12:08
 */
public class ObjectUtils {
    public static boolean isEmpty( Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional<?>) {
            Optional<?> optional = (Optional<?>) obj;
            return !optional.isPresent();
        }
        if (obj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) obj;
            return charSequence.length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) obj;
            return collection.isEmpty();
        }
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.isEmpty();
        }

        // else
        return false;
    }
}   
