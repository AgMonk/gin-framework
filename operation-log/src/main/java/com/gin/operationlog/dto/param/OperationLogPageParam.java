package com.gin.operationlog.dto.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gin.database.dto.param.BasePageParam;
import com.gin.database.dto.param.TimeRange;
import com.gin.operationlog.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 操作日志分页查询条件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 13:33
 */
@Getter
@Setter
@Schema(description = "操作日志分页查询条件")
public class OperationLogPageParam extends BasePageParam {
    @Schema(description = "主实体类型", hidden = true)
    @JsonIgnore
    String mainClassName;
    @Schema(description = "主实体ID")
    Long mainId;
    @Schema(description = "操作类型(多个用逗号隔开),选择副实体类型后,从其中的 types 字段中选择")
    List<OperationType> type;
    @Schema(description = "副实体类型,如果选择,应当从'日志选项'接口的返回值中选择")
    String subClassName;

    @Schema(description = "时间范围")
    TimeRange range;

    /**
     * 向queryWrapper中添加条件
     * @param queryWrapper 查询条件
     */
    @Override
    public void handleQueryWrapper(QueryWrapper<?> queryWrapper) {
        queryWrapper.orderByAsc("time_create");
        if (!ObjectUtils.isEmpty(mainClassName)) {
            queryWrapper.eq("main_class", mainClassName);
        }
        if (!CollectionUtils.isEmpty(type)) {
            queryWrapper.in("type", type.stream().map(Enum::name).toList());
        }
        if (!ObjectUtils.isEmpty(subClassName) && !subClassName.equals(mainClassName)) {
            queryWrapper.eq("sub_class", subClassName);
        } else {
            queryWrapper.isNull("sub_class");
        }
        if (mainId != null) {
            queryWrapper.eq("main_id", mainId);
        }
        if (range != null) {
            range.handleQueryWrapper(queryWrapper, "time_create");
        }
    }
}
