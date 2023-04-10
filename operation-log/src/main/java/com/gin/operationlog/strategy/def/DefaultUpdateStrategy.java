package strategy.def;

import annotation.LogStrategy;
import bo.OperationLogContext;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import strategy.AbstractUpdateStrategy;
import com.gin.common.utils.TimeUtils;
import com.gin.common.utils.reflect.FieldDifference;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认更新策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 10:41
 */
@Component
@LogStrategy(type = OperationType.UPDATE)
public class DefaultUpdateStrategy extends AbstractUpdateStrategy {

    /**
     * 过滤字段差异(筛选掉部分字段)
     * @param differences 字段差异
     * @return 字段差异
     */
    @Override
    public List<FieldDifference<Field, Object>> filter(List<FieldDifference<Field, Object>> differences) {
        return differences.stream().filter(dif -> {
            // 字段
            final Field field = dif.field();
            // 修改字段值
            final Object updateValue = dif.updateValue();

            final TableField tableField = field.getAnnotation(TableField.class);
            if (tableField == null || tableField.updateStrategy() == FieldStrategy.DEFAULT || tableField.updateStrategy() == FieldStrategy.NOT_NULL) {
                // 默认情况下, 修改值不为 null 则执行更新
                return updateValue != null;
            } else if (tableField.updateStrategy() == FieldStrategy.NOT_EMPTY) {
                // NOT_EMPTY 模式下 修改值不为 空 则执行更新
                return !ObjectUtils.isEmpty(updateValue);
            } else {
                // IGNORED 模式下总是更新 NEVER模式下总是不更新
                return tableField.updateStrategy() == FieldStrategy.IGNORED;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 格式化字段名
     * @param field 字段
     * @return 字段名
     */
    @Override
    public String formatField(Field field) {
        final Comment comment = field.getAnnotation(Comment.class);
        final Schema schema = field.getAnnotation(Schema.class);
        // 字段名翻译
        return comment != null ? comment.value() : (schema != null ? schema.description() : field.getName());
    }

    /**
     * 格式化字段值
     * @param field 字段
     * @param value 字段值
     * @return 字段值
     */
    @Override
    public String formatValue(Field field, Object value) {
        if (value instanceof Long time) {
            final String fieldName = field.getName();
            if (fieldName.contains("time") || "birthday".equals(fieldName)) {
                // 属于时间戳字段
                return TimeUtils.format(time);
            }
        }
        return String.valueOf(value);
    }

    /**
     * 获取修改前的实体对象
     * @param context 上下文
     * @return 修改前的实体对象
     */
    @Nullable
    @Override
    public Object getBeforeEntity(OperationLogContext context) {
        // 从preExp第1个元素传入
        final List<Object> list = context.preExp();
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取修改内容的实体对象
     * @param context 上下文
     * @return 修改内容的实体对象
     */
    @Nullable
    @Override
    public Object getUpdateEntity(OperationLogContext context) {
        // 从preExp第2个元素传入
        final List<Object> list = context.preExp();
        return list.size() > 1 ? list.get(1) : null;
    }
}
