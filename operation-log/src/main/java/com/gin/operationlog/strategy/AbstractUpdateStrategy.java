package com.gin.operationlog.strategy;

import com.gin.common.utils.reflect.FieldDifference;
import com.gin.common.utils.reflect.FieldValue;
import com.gin.common.utils.reflect.ReflectUtils;
import com.gin.operationlog.bo.OperationLogContext;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象更新策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 09:18
 */
@Slf4j
public abstract class AbstractUpdateStrategy implements DescriptionStrategy {

    /**
     * 生成描述(比较修改了哪些字段
     * @param context 上下文
     * @return 描述
     */
    @Override
    public final String generateDescription(OperationLogContext context) {
        final Object beforeEntity = getBeforeEntity(context);
        final Object updateEntity = getUpdateEntity(context);
        if (beforeEntity == null || updateEntity == null) {
            log.warn("两个实体不全, 无法比较, 原实体: {} 修改内容: {}", beforeEntity != null, updateEntity != null);
            return null;
        }
        if (!beforeEntity.getClass().equals(updateEntity.getClass())) {
            log.warn("两个实体的类型不同, 无法比较: {} -> {}", beforeEntity.getClass(), updateEntity.getClass());
            return null;
        }
        //获取他们的所有字段和字段值
        final List<FieldValue> beforeFieldValues = ReflectUtils.getAllFieldValues(beforeEntity);
        final List<FieldValue> updateFieldValues = ReflectUtils.getAllFieldValues(updateEntity);
        // 合并好的字段差异(已过滤掉值相同部分)
        final List<FieldDifference<Field, Object>> differences = FieldDifference.merge(beforeFieldValues, updateFieldValues);
        // 过滤掉部分字段
        final List<FieldDifference<Field, Object>> filteredDifferences = filter(differences);
        // 字段差异
        return filteredDifferences.size() == 0 ? "未做修改" : filteredDifferences.stream()
                // 字段差异格式化
                .map(dif -> {
                    final String fieldName = formatField(dif.field());
                    final String beforeValue = formatValue(dif.field(), dif.beforeValue());
                    final String updateValue = formatValue(dif.field(), dif.updateValue());
                    return new FieldDifference<>(fieldName, beforeValue, updateValue);
                })
                // 连接成字符串
                .map(d -> String.format("[%s] 从 '%s' 更新为 '%s'", d.field(), d.beforeValue(), d.updateValue()))
                .collect(Collectors.joining(", "));
    }

    /**
     * 过滤字段差异(筛选掉部分字段)
     * @param differences 字段差异
     * @return 字段差异
     */
    public abstract List<FieldDifference<Field, Object>> filter(List<FieldDifference<Field, Object>> differences);

    /**
     * 格式化字段值
     * @param field 字段
     * @param value 字段值
     * @return 字段值
     */
    public abstract String formatValue(Field field, Object value);

    /**
     * 格式化字段名
     * @param field 字段
     * @return 字段名
     */
    public abstract String formatField(Field field);

    /**
     * 获取修改内容的实体对象
     * @param context 上下文
     * @return 修改内容的实体对象
     */
    @Nullable
    public abstract Object getUpdateEntity(OperationLogContext context);

    /**
     * 获取修改前的实体对象
     * @param context 上下文
     * @return 修改前的实体对象
     */
    @Nullable
    public abstract Object getBeforeEntity(OperationLogContext context);

}
