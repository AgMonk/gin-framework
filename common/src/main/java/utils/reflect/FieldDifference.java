package utils.reflect;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段差异
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 10:19
 */
public record FieldDifference<F, V>(
        F field,
        V beforeValue,
        V updateValue
) {
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
            final Field field = bfv.field();
            // 原字段值
            final Object beforeValue = bfv.value();
            // 修改值
            final Object updateValue = update.stream()
                    .filter(f -> f.field().equals(field))
                    .map(FieldValue::value)
                    .toList().get(0);
            // 值不同的才加入
            if (!ObjectUtils.nullSafeEquals(beforeValue, updateValue)) {
                list.add(new FieldDifference<>(field, beforeValue, updateValue));
            }
        }
        return list;
    }

}
