package utils.reflect;

import java.lang.reflect.Field;

/**
 * 反射得到的字段和字段值
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 16:21
 */
public record FieldValue(
        Field field,
        Object value
) {

}   
