package com.gin.common.utils.reflect;

import com.gin.jackson.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段差异
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 10:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldDifference<F, V>{
    F field;
    V beforeValue;
    V updateValue;

    /**
     * 合并两个字段和字段值列表,并过滤掉值相同的字段
     * @param before 修改前字段和字段值
     * @param update 修改内容
     * @return 字段差异列表
     */
    public static List<FieldDifference<Field, Object>> merge(List<FieldValue> before, List<FieldValue> update) {
        final ArrayList<FieldDifference<Field, Object>> list = new ArrayList<>();
        for (FieldValue bfv : before) {
            // 字段
            final Field field = bfv.getField();
            // 原字段值
            final Object beforeValue = bfv.getValue();
            // 修改值
            final Object updateValue = update.stream()
                    .filter(f -> f.getField().equals(field))
                    .map(FieldValue::getValue)
                    .collect(Collectors.toList()).get(0);
            // 值不同的才加入
            if (!ObjectUtils.nullSafeEquals(beforeValue, updateValue)) {
                list.add(new FieldDifference<>(field, beforeValue, updateValue));
            }
        }
        return list;
    }

}
