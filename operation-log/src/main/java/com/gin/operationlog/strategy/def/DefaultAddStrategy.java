package com.gin.operationlog.strategy.def;

import com.gin.common.utils.TimeUtils;
import com.gin.common.utils.reflect.ReflectUtils;
import com.gin.spring.vo.response.Res;
import com.gin.database.base.BaseVo;
import com.gin.operationlog.annotation.LogStrategy;
import com.gin.operationlog.bo.OperationLogContext;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.strategy.DescriptionStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认添加描述策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 14:44
 */
@Component
@LogStrategy(type = OperationType.ADD)
public class DefaultAddStrategy implements DescriptionStrategy {
    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        // 如果 data 是 vo 类型， 使用 vo 的注解生成描述
        if (context.result() instanceof Res<?> res && res.getData() != null && res.getData() instanceof BaseVo vo) {
            List<String> des = new ArrayList<>();
            ReflectUtils.getAllFieldValues(vo).stream().filter(f -> f.getValue() != null).forEach(fieldValue -> {
                final Field field = fieldValue.getField();
                final Schema schema = field.getAnnotation(Schema.class);
                // 字段标题
                final String label = schema != null ? schema.description() : field.getName();
                // 字段值
                final String value = field.getName().contains("time") && field.getType().equals(Long.class) ? TimeUtils.format(((Long) fieldValue.getValue())) : String.valueOf(
                        fieldValue.getValue());
                des.add(String.format("%s: %s", label, value));
            });
            return String.join(", ", des);
        }
        return null;
    }
}
