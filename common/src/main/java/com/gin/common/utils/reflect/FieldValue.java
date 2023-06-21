package com.gin.common.utils.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;

/**
 * 反射得到的字段和字段值
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 16:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class FieldValue {
    Field field;
    Object value;


}   
